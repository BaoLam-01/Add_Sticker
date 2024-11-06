package vn.tapbi.sample2021kotlin.feature.cropwithshape

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import androidx.core.content.ContextCompat
import com.github.chrisbanes.photoview.OnMatrixChangedListener
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.OnScaleChangedListener
import com.github.chrisbanes.photoview.OnSingleFlingListener
import com.github.chrisbanes.photoview.OnViewDragListener
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.utils.BitmapUtils


class CropWithShape : androidx.appcompat.widget.AppCompatImageView {

    private val paintClear: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)
    private var shapeBitmap = BitmapFactory.decodeResource(resources, R.drawable.heart2)
    private var heartMask: Bitmap? = null
    private lateinit var actualBitmap: Bitmap

    private var attacher: PhotoViewAttacher? = null
    private var pendingScaleType: ScaleType? = null

    private val imgMatrix = Matrix()

    init {
        attacher = PhotoViewAttacher(this)


        //We always pose as a Matrix scale type, though we can change to another scale type
        //via the attacher
        super.setScaleType(ScaleType.MATRIX)

        //apply the previously applied scale type
        if (pendingScaleType != null) {
            scaleType = pendingScaleType as ScaleType
            pendingScaleType = null
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

//    override fun setImageBitmap(bitmap: Bitmap) {
//        imageBitmap = bitmap
//        requestLayout()
//    }

    fun setShapeBitmap(bitmap: Bitmap) {
        shapeBitmap = bitmap
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        actualBitmap = BitmapUtils.scaleBitmapAndKeepRation(imageBitmap, h, w)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
//
        super.onDraw(canvas)

        /*create shape overlay*/
        val overlayBitmap =
            Bitmap.createBitmap(actualBitmap.width, actualBitmap.height, Bitmap.Config.ARGB_8888)
        val overlayCanvas = Canvas(overlayBitmap)

        val modeClear: PorterDuff.Mode = PorterDuff.Mode.XOR
        paintClear.setXfermode(PorterDuffXfermode(modeClear))

        // draw overlay shape
        shapeBitmap?.let {
            overlayCanvas.drawColor(Color.WHITE)
            val shapeBitmap2 =
                BitmapUtils.scaleBitmapAndKeepRation(it, actualBitmap.height, actualBitmap.width)
            val centerX = (actualBitmap.width - shapeBitmap2.width) / 2
            val centerY = (actualBitmap.height - shapeBitmap2.height) / 2
            overlayCanvas.drawBitmap(shapeBitmap2, centerX.toFloat(), centerY.toFloat(), paintClear)
        }

        // draw overlay shape
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = ContextCompat.getColor(context, R.color.white2)

        canvas.drawBitmap(overlayBitmap, 0f, 0f, paint)

    }

    fun cutImageWithShape(): Bitmap {

        // Tạo một bitmap mới có cùng kích thước với ImageView
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val outputCanvas = Canvas(outputBitmap)

        // Vẽ hình ảnh vào canvas

        getSuppMatrix(imgMatrix)
        outputCanvas.drawBitmap(actualBitmap, imgMatrix, null)

        // Tạo một Paint với chế độ xóa
        val paint = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) // Chế độ xóa
        }

        // Vẽ mặt nạ hình trái tim lên canvas
        shapeBitmap?.let { it ->
            heartMask = BitmapUtils.scaleBitmapAndKeepRation(it, height, width)
            heartMask?.let { it1 ->
                outputCanvas.drawBitmap(it1, 0f, 0f, paint)
            }
        }

        return outputBitmap
    }

    fun getAttacher(): PhotoViewAttacher {
        return attacher!!
    }

    override fun getScaleType(): ScaleType {
        return attacher!!.scaleType
    }

    override fun getImageMatrix(): Matrix {
        return attacher!!.imageMatrix
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        attacher!!.setOnLongClickListener(l)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        attacher!!.setOnClickListener(l)
    }

    override fun setScaleType(scaleType: ScaleType) {
        if (attacher == null) {
            pendingScaleType = scaleType
        } else {
            attacher!!.scaleType = scaleType
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        // setImageBitmap calls through to this method
        if (attacher != null) {
            attacher!!.update()
        }
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (attacher != null) {
            attacher!!.update()
        }
        imageBitmap = BitmapFactory.decodeResource(resources, resId)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        if (attacher != null) {
            attacher!!.update()
        }
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        if (changed) {
            attacher!!.update()
        }
        return changed
    }

    fun setRotationTo(rotationDegree: Float) {
        attacher!!.setRotationTo(rotationDegree)
    }

    fun setRotationBy(rotationDegree: Float) {
        attacher!!.setRotationBy(rotationDegree)
    }

    fun isZoomable(): Boolean {
        return attacher!!.isZoomable
    }

    fun setZoomable(zoomable: Boolean) {
        attacher!!.isZoomable = zoomable
    }

    fun getDisplayRect(): RectF {
        return attacher!!.displayRect
    }

    fun getDisplayMatrix(matrix: Matrix?) {
        attacher!!.getDisplayMatrix(matrix)
    }

    fun setDisplayMatrix(finalRectangle: Matrix?): Boolean {
        return attacher!!.setDisplayMatrix(finalRectangle)
    }

    fun getSuppMatrix(matrix: Matrix?) {
        attacher!!.getSuppMatrix(matrix)
    }

    fun setSuppMatrix(matrix: Matrix?): Boolean {
        return attacher!!.setDisplayMatrix(matrix)
    }

    fun getMinimumScale(): Float {
        return attacher!!.minimumScale
    }

    fun getMediumScale(): Float {
        return attacher!!.mediumScale
    }

    fun getMaximumScale(): Float {
        return attacher!!.maximumScale
    }

    fun getScale(): Float {
        return attacher!!.scale
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        attacher!!.setAllowParentInterceptOnEdge(allow)
    }

    fun setMinimumScale(minimumScale: Float) {
        attacher!!.minimumScale = minimumScale
    }

    fun setMediumScale(mediumScale: Float) {
        attacher!!.mediumScale = mediumScale
    }

    fun setMaximumScale(maximumScale: Float) {
        attacher!!.maximumScale = maximumScale
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        attacher!!.setScaleLevels(minimumScale, mediumScale, maximumScale)
    }

    fun setOnMatrixChangeListener(listener: OnMatrixChangedListener?) {
        attacher!!.setOnMatrixChangeListener(listener)
    }

    fun setOnPhotoTapListener(listener: OnPhotoTapListener?) {
        attacher!!.setOnPhotoTapListener(listener)
    }

    fun setOnOutsidePhotoTapListener(listener: OnOutsidePhotoTapListener?) {
        attacher!!.setOnOutsidePhotoTapListener(listener)
    }

    fun setOnViewTapListener(listener: OnViewTapListener?) {
        attacher!!.setOnViewTapListener(listener)
    }

    fun setOnViewDragListener(listener: OnViewDragListener?) {
        attacher!!.setOnViewDragListener(listener)
    }

    fun setScale(scale: Float) {
        attacher!!.scale = scale
    }

    fun setScale(scale: Float, animate: Boolean) {
        attacher!!.setScale(scale, animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        attacher!!.setScale(scale, focalX, focalY, animate)
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        attacher!!.setZoomTransitionDuration(milliseconds)
    }

    fun setOnDoubleTapListener(onDoubleTapListener: GestureDetector.OnDoubleTapListener?) {
        attacher!!.setOnDoubleTapListener(onDoubleTapListener)
    }

    fun setOnScaleChangeListener(onScaleChangedListener: OnScaleChangedListener?) {
        attacher!!.setOnScaleChangeListener(onScaleChangedListener)
    }

    fun setOnSingleFlingListener(onSingleFlingListener: OnSingleFlingListener?) {
        attacher!!.setOnSingleFlingListener(onSingleFlingListener)
    }
}
package vn.tapbi.sample2021kotlin.feature.adjustcut

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.core.graphics.drawable.toBitmap
import com.github.chrisbanes.photoview.OnViewDrawListener
import com.github.chrisbanes.photoview.PhotoView
import vn.tapbi.sample2021kotlin.utils.BitmapUtils

class AdjustCutView : PhotoView,
    OnViewDrawListener {

    private var currentX: Float = 0f
    private var currentY: Float = 0f

    private var isPaintEraser = true

    private var imageBitmap: Bitmap? = null
    private var sourceBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null
    private var paintCanvas: Canvas? = null
    private var path = Path()

    private var actualVisibleBitmap: Bitmap? = null

    private var distance: Float = 0f

    private var bitmapOffsetX = 0f
    private var bitmapOffsetY = 0f

    private var matrix: Matrix = Matrix()

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 50f
        color = Color.WHITE
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }


    fun setEraseMode(isErasing: Boolean) {
        if (isErasing) {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        } else {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        }
        isPaintEraser = isErasing
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attacher.setOnViewMoveListener(this)
    }

    fun setPaintEraser(boolean: Boolean) {
        isPaintEraser = boolean
    }

    fun getPaintEraser(): Boolean {
        return isPaintEraser
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        imageBitmap?.let {
            actualVisibleBitmap = BitmapUtils.scaleBitmapAndKeepRation(imageBitmap, w, h)
            actualVisibleBitmap?.let {
                sourceBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                tempBitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
                paintCanvas = Canvas(tempBitmap!!)
//                canvas?.drawBitmap(sourceBitmap!!, 0f, 0f, null)

                bitmapOffsetX = (width - it.width) / 2f
                bitmapOffsetY = (height - it.height) / 2f
            }
        }
    }


    override fun setImageDrawable(drawable: Drawable?) {
        imageBitmap = drawable?.toBitmap()
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        imageBitmap = BitmapFactory.decodeResource(resources, resId)
        super.setImageResource(resId)
    }


    override fun setImageURI(uri: Uri?) {
        imageBitmap = BitmapUtils.getBitmapFromUri(uri, context)
        super.setImageURI(uri)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        tempBitmap?.let {
            getSuppMatrix(matrix)
            canvas.drawBitmap(it, matrix, null)
        }

    }


    fun setSizePaint(size: Float) {
        paint.strokeWidth = size
    }

    fun setDistance(distance: Float) {
        this.distance = distance
    }

    override fun onTouchDown(x: Float, y: Float) {


        path.moveTo(x,y)
        currentX = x
        currentY = y


    }

    override fun onMove(x: Float, y: Float) {
        path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)

        paintCanvas?.drawPath(path, paint)
        val unEraserPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        unEraserPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        currentX = x
        currentY = y

        invalidate()
    }


}
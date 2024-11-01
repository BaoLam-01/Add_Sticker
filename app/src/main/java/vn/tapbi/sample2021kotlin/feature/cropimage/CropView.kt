package vn.tapbi.sample2021kotlin.feature.cropimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import timber.log.Timber
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.feature.stickerview.BitmapStickerIcon
import vn.tapbi.sample2021kotlin.feature.stickerview.Sticker
import vn.tapbi.sample2021kotlin.feature.stickerview.StickerView
import vn.tapbi.sample2021kotlin.feature.stickerview.ZoomIconEvent
import vn.tapbi.sample2021kotlin.utils.BitmapUtils
import kotlin.math.pow
import kotlin.math.sqrt

class CropView : StickerView {

    private var bitmapImage: Bitmap? = null
    private var imageView: ImageView = ImageView(this.context)
    private lateinit var cropOverlay: CropOverlay
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var viewCanvasWidth = 0
    private var viewCanvasHeight = 0
    private var actualVisibleBitmap: Bitmap? = null
    private var limitScale: Float = 0f;

    private lateinit var zoomIcon: BitmapStickerIcon

    private val overlayPaint = Paint().apply {
        color = Color.BLACK
        alpha = 100 // set alpha
    }

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun configDefaultIcons() {
        zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(context, R.drawable.zoom_sticker_icon),
            BitmapStickerIcon.RIGHT_BOTOM
        )
        zoomIcon.iconEvent = ZoomIconEvent()

        icons.clear()
        icons.add(zoomIcon)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(imageView)
    }

    fun setBitmapImage(bitmap: Bitmap) {

        bitmapImage = bitmap
        actualVisibleBitmap = null

        imageView.setImageBitmap(bitmap)
        imageView.adjustViewBounds = true

        val drawable = ContextCompat.getDrawable(context, R.drawable.crop_overlay)
        if (drawable != null) {
            cropOverlay = CropOverlay(drawable)
            addSticker(cropOverlay)
        }

    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        viewCanvasWidth = canvas.width
        viewCanvasHeight = canvas.height

        if (bitmapImage != null) {
            // If target's original bitmap is bigger than view size, adjust size for fit
            if (actualVisibleBitmap == null) actualVisibleBitmap = scaleBitmapAndKeepRation(
                bitmapImage!!,
                height,
                width
            )

            val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

            val path = getPathFromBitmapPoint2()

            canvas.drawPath(path, clearPaint)
            canvas.drawCircle(
                zoomIcon.x,
                zoomIcon.y,
                zoomIcon.drawable.intrinsicHeight / 2f,
                clearPaint
            )

            canvas.restoreToCount(saveCount)

        } else {
            canvas.drawColor(Color.WHITE)

            val textPaint = Paint()
            textPaint.color = Color.BLACK
            textPaint.textSize = 20f
            textPaint.isAntiAlias = true

            canvas.drawText(
                "Please set image bitmap for process",
                (width / 3).toFloat(),
                (height / 2).toFloat(),
                textPaint
            )
        }
        limitScale = sqrt(width.toDouble().pow(2.0) + height.toDouble().pow(2.0)).toFloat()
        Timber.e("limitScale: $limitScale")

    }

    fun scaleBitmapAndKeepRation(
        TargetBmp: Bitmap,
        reqHeightInPixels: Int,
        reqWidthInPixels: Int
    ): Bitmap {
        val m = Matrix()
        m.setRectToRect(
            RectF(0f, 0f, TargetBmp.width.toFloat(), TargetBmp.height.toFloat()),
            RectF(0f, 0f, reqWidthInPixels.toFloat(), reqHeightInPixels.toFloat()),
            Matrix.ScaleToFit.CENTER
        )
        return Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.width, TargetBmp.height, m, true)
    }

    override fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return 0f
    }

    override fun actionZoomWithTwoFinger(event: MotionEvent) {
        if (handlingSticker != null) {
            val newDistance = calculateDistance(event)
            val newRotation = calculateRotation(event)
            var scale = newDistance / oldDistance

            if (limitScale != 0f) {
                scale = limitScale(scale, limitScale)
            }

            moveMatrix.set(downMatrix)
            moveMatrix.postScale(
                scale, scale, midPoint.x,
                midPoint.y
            )
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
            handlingSticker.setMatrix(moveMatrix)
        }

    }

    override fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
        if (sticker != null) {

            val scaleX = event.x / downX
            val scaleY = event.y / downY

            moveMatrix.set(downMatrix)
            moveMatrix.postScale(scaleX, scaleY)
            handlingSticker.setMatrix(moveMatrix)
        }
    }

    override fun constrainSticker(sticker: Sticker) {
        var moveX = 0f
        var moveY = 0f
        val width = width
        val height = height
        val stickerWidth = bitmapPoints[2] - bitmapPoints[0]
        val stickerHeight = bitmapPoints[5] - bitmapPoints[1]
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp)
        if (currentCenterPoint.x < stickerWidth / 2) {
            moveX = -currentCenterPoint.x + stickerWidth / 2
        }

        if (currentCenterPoint.x > (width - stickerWidth / 2)) {
            moveX = width - stickerWidth / 2 - currentCenterPoint.x
        }

        if (currentCenterPoint.y < stickerHeight / 2) {
            moveY = -currentCenterPoint.y + stickerHeight / 2
        }

        if (currentCenterPoint.y > (height - stickerHeight / 2)) {
            moveY = height - stickerHeight / 2 - currentCenterPoint.y
        }

        sticker.matrix.postTranslate(moveX, moveY)
    }

    fun cropImage() : Bitmap? {
        actualVisibleBitmap?.let {
            return BitmapUtils.getCroppedBitmap(it, getPathFromBitmapPoint(), bitmapPoints[0], bitmapPoints[1])
        }
        return null
    }

    fun cropImageWithPath(): Bitmap? {

        actualVisibleBitmap?.let {


            val resultImage =
                Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)

            val resultCanvas = Canvas(resultImage)
            val resultPaint = Paint()


            // struct paint for naturally
            resultPaint.isDither = true // set the dither to true
            resultPaint.strokeJoin = Paint.Join.ROUND // set the join to round you want
            resultPaint.strokeCap = Paint.Cap.ROUND // set the paint cap to round too
//            resultPaint.setPathEffect(CornerPathEffect(10f))
            resultPaint.isAntiAlias = true // set anti alias so it smooths


            // struct paint for path-crop

            val path = getPathFromBitmapPoint()

            resultCanvas.drawPath(path, resultPaint)
            resultPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

            val dst = Rect(
                viewCanvasWidth / 2 - it.width / 2,
                viewCanvasHeight / 2 - it.height / 2,
                viewCanvasWidth / 2 + it.width / 2,
                viewCanvasHeight / 2 + it.height / 2
            )

            resultCanvas.drawBitmap(it, null, dst, resultPaint)
//            resultCanvas.drawBitmap(actualVisibleBitmap!!,0f,0f, resultPaint)

            return BitmapUtils.cropBitmapToBoundingBox(resultImage, Color.TRANSPARENT)
        }

        return null
    }


    private fun getPathFromBitmapPoint(): Path {
        var x1 = bitmapPoints[0]
        var y1 = bitmapPoints[1]
        var x2 = bitmapPoints[2]
        var y2 = bitmapPoints[3]
        var x3 = bitmapPoints[4]
        var y3 = bitmapPoints[5]
        var x4 = bitmapPoints[6]
        var y4 = bitmapPoints[7]

        if (x4 > height) {
            x2 = width.toFloat()
            x4 = width.toFloat()
        }

        if (y4 > height) {
            y3 = height.toFloat()
            y4 = height.toFloat()
        }


        val path = Path()
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x4, y4)
        path.lineTo(x3, y3)
        return path
    }

    private fun getPathFromBitmapPoint2(): Path {
        var x1 = bitmapPoints[0] - 2
        var y1 = bitmapPoints[1] - 2
        var x2 = bitmapPoints[2] + 2
        var y2 = bitmapPoints[3] - 2
        var x3 = bitmapPoints[4] - 2
        var y3 = bitmapPoints[5] + 2
        var x4 = bitmapPoints[6] + 2
        var y4 = bitmapPoints[7] + 2

        if (x4 > height) {
            x2 = width.toFloat()
            x4 = width.toFloat()
        }

        if (y4 > height) {
            y3 = height.toFloat()
            y4 = height.toFloat()
        }


        val path = Path()
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x4, y4)
        path.lineTo(x3, y3)
        return path
    }


}
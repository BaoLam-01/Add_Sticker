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
import android.view.MotionEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
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

    private var viewCanvasWidth = 0
    private var viewCanvasHeight = 0
    private var actualVisibleBitmap: Bitmap? = null
    private var limitScale: Float = 0f;


    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        limitScale = sqrt(width.toDouble().pow(2.0) + height.toDouble().pow(2.0)).toFloat()
    }

    override fun configDefaultIcons() {
        val zoomIcon = BitmapStickerIcon(
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

    override fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
        if (sticker != null) {
            val newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
            val newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
//            var scale = newDistance / oldDistance
//
//            if (limitScale != 0f) {
//                scale = limitScale(scale, limitScale)
//            }

            val scaleX = scaleX(downX, event.x)
            val scaleY = scaleX(downY, event.y)

            moveMatrix.set(downMatrix)
            moveMatrix.postScale(
                scaleX, scaleY, bitmapPoints[0],
                bitmapPoints[1]
            )
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
            handlingSticker.setMatrix(moveMatrix)
        }
    }



    fun cropImageWithPath(): Bitmap? {

        bitmapImage?.let {


            val resultImage =
                Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)

            val resultCanvas = Canvas(resultImage)
            val resultPaint = Paint()


            // struct paint for naturally
            resultPaint.isAntiAlias = true
            resultPaint.isDither = true // set the dither to true
            resultPaint.strokeJoin = Paint.Join.ROUND // set the join to round you want
            resultPaint.strokeCap = Paint.Cap.ROUND // set the paint cap to round too
            resultPaint.setPathEffect(CornerPathEffect(10f))
            resultPaint.isAntiAlias = true // set anti alias so it smooths


            // struct paint for path-crop

            cropOverlay.getMappedPoints(bitmapPoints)

            val x1 = bitmapPoints[0]
            val y1 = bitmapPoints[1]
            val x2 = bitmapPoints[2]
            val y2 = bitmapPoints[3]
            val x3 = bitmapPoints[4]
            val y3 = bitmapPoints[5]
            val x4 = bitmapPoints[6]
            val y4 = bitmapPoints[7]

            val path = Path()
            path.reset()
            path.moveTo(x1, y1)
            path.lineTo(x2, y2)
            path.lineTo(x4, y4)
            path.lineTo(x3, y3)

            resultCanvas.drawPath(path, resultPaint)
            resultPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

            val dst = Rect(
                viewCanvasWidth / 2 - actualVisibleBitmap!!.width / 2,
                viewCanvasHeight / 2 - actualVisibleBitmap!!.height / 2,
                viewCanvasWidth / 2 + actualVisibleBitmap!!.width / 2,
                viewCanvasHeight / 2 + actualVisibleBitmap!!.height / 2
            )

            resultCanvas.drawBitmap(actualVisibleBitmap!!, null, dst, resultPaint)

            return BitmapUtils.cropBitmapToBoundingBox(resultImage, Color.TRANSPARENT)
        }



        return null
    }


}
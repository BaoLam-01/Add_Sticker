package vn.tapbi.sample2021kotlin.feature.adjustcut

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import vn.tapbi.sample2021kotlin.utils.BitmapUtils
import kotlin.math.atan2
import kotlin.math.sqrt

class AdjustCutView : AppCompatImageView {

    private var imageBitmap: Bitmap? = null

    private var oldDistance: Float = 0f
    private var oldRotation: Float = 0f
    private var midPoint: PointF = PointF()
    private var limitScale = 1080f
    private var oldSize = 0f

    private var bitmapPoints: FloatArray = FloatArray(8)
    private var downMatrix = Matrix()
    private var moveMatrix = Matrix()

    private var currentX: Float = 0f
    private var currentY: Float = 0f

    private var isPaintEraser = true

    private val paths = mutableListOf<PathWithPaint>()

    private val draw: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        strokeWidth = 50f
        isDither = true
    }


    private val eraser: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        strokeWidth = 50f
        isDither = true

    }
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        isAntiAlias = true
    }

    private var path: Path = Path()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setPaintEraser(boolean: Boolean) {
        isPaintEraser = boolean
    }

    fun getPaintEraser(): Boolean {
        return isPaintEraser
    }

    override fun setImageBitmap(bm: Bitmap?) {
        imageBitmap = bm
        requestLayout()
        invalidate()
        super.setImageBitmap(bm)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
        imageBitmap?.let {
            val viewCanvasWidth = width
            val viewCanvasHeight = height

            val drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val outputCanvas = Canvas(drawBitmap)

            val actualVisibleBitmap = BitmapUtils.scaleBitmapAndKeepRation(it, height, width)


//            for (pathWithPaint in paths) {
//                outputCanvas.drawPath(pathWithPaint.path, pathWithPaint.paint)
//            }

            val paint = if (isPaintEraser) eraser else draw
            outputCanvas.drawPath(path, paint)

            canvas.drawBitmap(
                actualVisibleBitmap,
                (viewCanvasWidth / 2 - actualVisibleBitmap.width / 2).toFloat(),
                (viewCanvasHeight / 2 - actualVisibleBitmap.height / 2).toFloat(),
                null
            )
            // draw new bitmap to canvas
            canvas.drawBitmap(drawBitmap, 0f, 0f, bitmapPaint)


        }


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (imageBitmap == null) {
            return false
        }

        event?.let {
            val x = event.x
            val y = event.y

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    currentX = x
                    currentY = y


                    oldDistance = calculateDistance(midPoint.x, midPoint.y, x, y)
                    oldRotation = calculateRotation(midPoint.x, midPoint.y, x, y)
                    oldSize = calculateDistance(
                        bitmapPoints[0],
                        bitmapPoints[1],
                        bitmapPoints[6],
                        bitmapPoints[7]
                    )

                    downMatrix = imageMatrix
                    path = Path()
                    path.moveTo(x, y)

                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)

                    currentX = x
                    currentY = y

                }

                MotionEvent.ACTION_UP -> {
                    path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)

                    val paint = if (isPaintEraser) eraser else draw

                    paths.add(PathWithPaint(path, paint))

//                    path = Path()
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    return true
                }
            }
            invalidate()
            return true
        }

        return super.onTouchEvent(event)

    }

    protected fun actionZoomWithTwoFinger(event: MotionEvent) {
        val newDistance: Float = calculateDistance(event)
        val newRotation: Float = calculateRotation(event)

        var scale: Float = newDistance / oldDistance

        if (limitScale != 0f) {
            scale = limitScale(scale, limitScale)
        }


        moveMatrix.set(downMatrix)
        moveMatrix.postScale(
            scale, scale, midPoint.x,
            midPoint.y
        )
        moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
        imageMatrix = moveMatrix

    }

    protected fun calculateDistance(event: MotionEvent?): Float {
        if (event == null || event.pointerCount < 2) {
            return 0f
        }
        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        return sqrt(x * x + y * y).toFloat()
    }

    protected fun calculateRotation(event: MotionEvent?): Float {
        if (event == null || event.pointerCount < 2) {
            return 0f
        }
        return calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        val radians = atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    protected fun limitScale(scale: Float, scaleLimit: Float): Float {
        var scale = scale
        if (scale * oldSize >= scaleLimit) {
            scale = scaleLimit / oldSize
        } else if (scale * oldSize <= 200) {
            scale = 200 / oldSize
        }
        return scale
    }

    data class PathWithPaint(val path: Path, val paint: Paint)

}
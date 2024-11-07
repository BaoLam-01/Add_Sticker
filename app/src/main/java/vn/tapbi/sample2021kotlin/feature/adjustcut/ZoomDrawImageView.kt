package vn.tapbi.sample2021kotlin.feature.adjustcut

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import vn.tapbi.sample2021kotlin.R

class ZoomDrawImageView : View {
    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val path = Path()
    private val paths = mutableListOf<Path>()

    private var scaleFactor = 1.0f
    private var dragFactorX = 1.0f
    private var dragFactorY = 1.0f
    private var currentX = 1.0f
    private var currentY = 1.0f
    private var lastX = 0f
    private var lastY = 0f
    private val scaleGestureDetector =
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {


                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(1.0f, 5.0f) // Giới hạn zoom
                invalidate()
                return true
            }

        })


    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f)
//        canvas.translate(dragFactorX, dragFactorY)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)

        canvas.drawBitmap(bitmap, 0f, 0f, null)

        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)

//        if (event.pointerCount > 1) return true // Dừng vẽ khi có nhiều hơn một ngón tay

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1) {
                    path.lineTo(event.x, event.y)
                } else {
                    dragFactorX = event.x - lastX
                    dragFactorY = event.y - lastY

                    path.moveTo(event.getX(1), event.getY(1))
                    currentX = event.x
                    currentY = event.y
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                paths.add(Path(path))
                path.reset()
            }
        }
        return true
    }

}

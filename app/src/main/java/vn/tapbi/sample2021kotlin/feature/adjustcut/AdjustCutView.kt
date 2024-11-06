package vn.tapbi.sample2021kotlin.feature.adjustcut

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.Display
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import vn.tapbi.sample2021kotlin.utils.BitmapUtils
import vn.tapbi.sample2021kotlin.utils.Utils

class AdjustCutView : AppCompatImageView {

    private var currentX: Float = 0f
    private var currentY: Float = 0f

    private var isPaintEraser = true

    private var imageBitmap: Bitmap? =  null
    private var sourceBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var path = Path()

    private var actualVisibleBitmap: Bitmap? = null

    private var distance : Float = 0f

    private var bitmapOffsetX = 0f
    private var bitmapOffsetY = 0f


    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 50f
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    fun setEraseMode(isErasing: Boolean) {
        if (isErasing) {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        isPaintEraser = isErasing
    }


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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        actualVisibleBitmap = BitmapUtils.scaleBitmapAndKeepRation(imageBitmap, w, h)
        actualVisibleBitmap?.let {
            sourceBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
            tempBitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            canvas = Canvas(tempBitmap!!)
            canvas?.drawBitmap(sourceBitmap!!, 0f, 0f, null)

            bitmapOffsetX = (width - it.width) / 2f
            bitmapOffsetY = (height - it.height) / 2f
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
//        super.setImageBitmap(bm)
        imageBitmap = bm
        requestLayout()
        invalidate()


    }



    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
        tempBitmap?.let {
            canvas.drawBitmap(it, bitmapOffsetX, bitmapOffsetY, null)
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (actualVisibleBitmap == null) {
            return false
        }

        event?.let {
            val x = event.x - bitmapOffsetX
            val y = event.y - bitmapOffsetY

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    currentX = x
                    currentY = y
                    path.moveTo(x, y)

                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    return true
                }

                MotionEvent.ACTION_MOVE -> {

                    path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)

                    sourceBitmap?.let { source ->
                        canvas?.drawPath(path, paint)
                        val unEraserPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                        unEraserPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
                        canvas?.drawBitmap(source, 0f, 0f, unEraserPaint)
                    }
                    currentX = x
                    currentY = y

                }

                MotionEvent.ACTION_UP -> {
                    path.reset()
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

    fun setSizePaint(size: Float){
        paint.strokeWidth = size
    }

    fun setDistance(distance: Float) {
        this.distance = distance
    }


}
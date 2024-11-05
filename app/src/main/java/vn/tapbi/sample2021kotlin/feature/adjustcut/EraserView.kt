package vn.tapbi.sample2021kotlin.feature.adjustcut

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class EraserView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var sourceBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 50f
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) // Chế độ xóa
    }

    private var lastX = 0f
    private var lastY = 0f

    // Thiết lập hình ảnh nguồn
    fun setImage(bitmap: Bitmap) {
        sourceBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        tempBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(tempBitmap!!)
        canvas?.drawBitmap(sourceBitmap!!, 0f, 0f, null)
        invalidate()
    }

    // Chuyển đổi giữa chế độ xóa và khôi phục
    fun setEraseMode(isErasing: Boolean) {
        if (isErasing) {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        tempBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                lastX = x
                lastY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
                
                if (paint.xfermode.equals(PorterDuff.Mode.SRC) ){
                    // Khi khôi phục, vẽ lại từ bitmap gốc
                    sourceBitmap?.let { source ->
                        canvas?.drawPath(path, paint)
                        canvas?.drawBitmap(source, 0f, 0f, paint)
                    }
                } else {
                    // Khi xóa
                    canvas?.drawPath(path, paint)
                }
                
                lastX = x
                lastY = y
                invalidate()
                path.reset()
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                path.reset()
            }
        }
        return super.onTouchEvent(event)
    }
}
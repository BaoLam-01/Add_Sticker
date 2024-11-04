package vn.tapbi.sample2021kotlin.feature.cropwithshape

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.utils.BitmapUtils


class CropWithShape : androidx.appcompat.widget.AppCompatImageView {

    private val paintImage: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintClear: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)
    private var shapeBitmap = BitmapFactory.decodeResource(resources, R.drawable.heart2)
    private var heartMask: Bitmap? = null
    private lateinit var actualBitmap: Bitmap

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setImageBitmap(bitmap: Bitmap) {
        imageBitmap = bitmap
        requestLayout()
    }

    fun setShapeBitmap(bitmap: Bitmap) {
        shapeBitmap = bitmap
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        imageBitmap?.let {
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
                setMeasuredDimension(it.width, measureSize(heightMeasureSpec))
            } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                setMeasuredDimension(measureSize(widthMeasureSpec), it.height)
            } else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST && MeasureSpec.getMode(
                    heightMeasureSpec
                ) == MeasureSpec.AT_MOST
            ){
                setMeasuredDimension(it.width, it.height)
            } else {
                setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
            }
        } ?: setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
    }

    private fun measureSize(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> size.coerceAtMost(500)
            else -> 0
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
//
        // draw image
        imageBitmap?.let {
            actualBitmap = BitmapUtils.scaleBitmapAndKeepRation(it, height, width)
            canvas.drawBitmap(actualBitmap, 0f, 0f, paintImage)
        } ?: return

        /*create shape overlay*/
        val outputBitmap =
            Bitmap.createBitmap(actualBitmap.width, actualBitmap.height, Bitmap.Config.ARGB_8888)
        val outputCanvas = Canvas(outputBitmap)

        val modeClear: PorterDuff.Mode = PorterDuff.Mode.XOR
        paintClear.setXfermode(PorterDuffXfermode(modeClear))

        // draw overlay shape
        shapeBitmap?.let {
            outputCanvas.drawColor(Color.WHITE)
            val shapeBitmap2 =
                BitmapUtils.scaleBitmapAndKeepRation(it, actualBitmap.height, actualBitmap.width)
            val centerX = (actualBitmap.width - shapeBitmap2.width) / 2
            val centerY = (actualBitmap.height - shapeBitmap2.height) / 2
            outputCanvas.drawBitmap(shapeBitmap2, centerX.toFloat(), centerY.toFloat(), paintClear)
        }

        // draw overlay shape
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = ContextCompat.getColor(context, R.color.white2)

        canvas.drawBitmap(outputBitmap, 0f, 0f, paint)

    }

    fun cutImageWithShape(): Bitmap {

        // Tạo một bitmap mới có cùng kích thước với ImageView
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val outputCanvas = Canvas(outputBitmap)

        // Vẽ hình ảnh vào canvas
        imageBitmap?.let {
            val bitmap2 = BitmapUtils.scaleBitmapAndKeepRation(it, height, width)
            outputCanvas.drawBitmap(bitmap2, 0f, 0f, null)
        }

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


}
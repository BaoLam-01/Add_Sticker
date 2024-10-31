package vn.tapbi.sample2021kotlin.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@SuppressLint("AppCompatCustomView")
@AndroidEntryPoint
class RoundedImageView : ImageView {

    var sharedPreferenceHelper: SharedPreferences? = null
        @Inject set

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDraw(canvas: Canvas) {
        val path = Path()
        path.addRoundRect(
            RectF(18F, 18F, (width - 18).toFloat(), (height - 18).toFloat()),
            18f,
            18f,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}

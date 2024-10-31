package vn.tapbi.sample2021kotlin.feature.cropimage

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import vn.tapbi.sample2021kotlin.feature.stickerview.Sticker

class CropOverlay(private var drawable: Drawable) : Sticker() {
    private var realBounds: Rect? = null

    init {
        realBounds = Rect(0, 0, width, height)
    }

    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun setDrawable(drawable: Drawable): CropOverlay {
        this.drawable = drawable
        return this
    }


    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.concat(matrix)
        drawable.bounds = realBounds!!
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): CropOverlay {
        drawable.alpha = alpha
        return this
    }

    override fun getWidth(): Int {
        return drawable.intrinsicWidth
    }

    override fun getHeight(): Int {
        return drawable.intrinsicHeight
    }

    override fun release() {
        super.release()
    }
}
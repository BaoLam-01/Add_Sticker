package vn.tapbi.sample2021kotlin.feature

import android.content.Context
import android.util.AttributeSet
import android.view.View
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VisualizerView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }
}
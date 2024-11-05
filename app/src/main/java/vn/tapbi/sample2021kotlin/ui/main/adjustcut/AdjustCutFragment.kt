package vn.tapbi.sample2021kotlin.ui.main.adjustcut

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentAdjustCutBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment

class AdjustCutFragment : BaseBindingFragment<FragmentAdjustCutBinding, AdjustCutViewModel>() {
    override fun getViewModel(): Class<AdjustCutViewModel> {
        return AdjustCutViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_adjust_cut

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {
        val bm = BitmapFactory.decodeResource(resources, R.drawable.meo)
        binding.adjustCut.setImageBitmap(bm)

        binding.btnSwap.setOnClickListener {
            binding.adjustCut.setPaintEraser(!binding.adjustCut.getPaintEraser())
        }
    }

    override fun onPermissionGranted() {
    }
}
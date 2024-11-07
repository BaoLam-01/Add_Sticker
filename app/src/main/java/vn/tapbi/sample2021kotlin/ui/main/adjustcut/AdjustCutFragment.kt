package vn.tapbi.sample2021kotlin.ui.main.adjustcut

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentAdjustCutBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment

class AdjustCutFragment : BaseBindingFragment<FragmentAdjustCutBinding, AdjustCutViewModel>() {

    private var isErasing = true

    override fun getViewModel(): Class<AdjustCutViewModel> {
        return AdjustCutViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_adjust_cut

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {


//        arguments?.let {
//            val strUri = it.getString("outUri")
//            val bitmap = BitmapUtils.getBitmapFromUri(Uri.parse(strUri), context)
//
//            binding.adjustCutView.setImageBitmap(bitmap)
//
//            binding.btnSwap.setOnClickListener {
//                isErasing = !isErasing
//                binding.adjustCutView.setEraseMode(isErasing)
//            }
//        }

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)
        binding.adjustCutView.setImageBitmap(bitmap)
//        binding.adjustCutView.setImageResource(R.drawable.meo)
        binding.adjustCutView.setDragging(false)
        binding.adjustCutView.setDoubleClickToZoom(false)
        binding.adjustCutView.setAllowParentInterceptOnEdge(false)

//
        binding.btnSwap.setOnClickListener {
            isErasing = !binding.adjustCutView.getPaintEraser()
            binding.adjustCutView.setEraseMode(isErasing)
        }
//

    }

    override fun onPermissionGranted() {
    }
}
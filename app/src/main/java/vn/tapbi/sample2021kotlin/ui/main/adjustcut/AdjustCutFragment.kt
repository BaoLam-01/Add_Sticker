package vn.tapbi.sample2021kotlin.ui.main.adjustcut

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentAdjustCutBinding
import vn.tapbi.sample2021kotlin.feature.adjustcut.EraserView
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import vn.tapbi.sample2021kotlin.utils.BitmapUtils

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
//
//            val bitmap = BitmapFactory.decodeResource(resources,R.drawable.meo)
//
//
//            binding.adjustCutView.setImageBitmap(bitmap)
//
//            binding.btnSwap.setOnClickListener {
//                isErasing = !isErasing
//                binding.adjustCutView.setEraseMode(isErasing)
//            }


        binding.photoView.setImageResource(R.drawable.meo);


    }

    override fun onPermissionGranted() {
    }
}
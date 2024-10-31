package vn.tapbi.sample2021kotlin.ui.main.cropimage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentCropImageBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import vn.tapbi.sample2021kotlin.utils.BitmapUtils

class CropImageFragment : BaseBindingFragment<FragmentCropImageBinding, CropImageViewModel>() {
    override fun getViewModel(): Class<CropImageViewModel> {
        return CropImageViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_crop_image

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)

        binding.cropView.setBitmapImage(bitmap)
        binding.cropView.setConstrained(true)

        binding.tvNext.setOnClickListener {
            val bit = binding.cropView.cropImageWithPath()
            val uri = BitmapUtils.saveBitmapToFile(bit, context)

            val bundle = Bundle()
            bundle.putString("uriImage", uri.toString())
            Navigation.findNavController(binding.root).navigate(R.id.showCutFragment, bundle)
        }

    }

    override fun onPermissionGranted() {
    }
}
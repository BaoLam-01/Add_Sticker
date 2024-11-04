package vn.tapbi.sample2021kotlin.ui.main.cropwithshape

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentCropWithShapeBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import vn.tapbi.sample2021kotlin.utils.BitmapUtils

class CropWithShapeFragment :
    BaseBindingFragment<FragmentCropWithShapeBinding, CropWithShapeViewModel>() {
    override fun getViewModel(): Class<CropWithShapeViewModel> {
        return CropWithShapeViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_crop_with_shape

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {

        binding.btnCut.setOnClickListener {
            val outBitmap = binding.cropWithShape.cutImageWithShape()
            val outUri = BitmapUtils.saveBitmapToFile(outBitmap, context)
            val bundle = Bundle()
            bundle.putString("outUri", outUri.toString())

            Navigation.findNavController(binding.root).navigate(R.id.showCutFragment, bundle)

        }
        binding.btnStar.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.star)
            binding.cropWithShape.setShapeBitmap(bitmap)
        }
    }

    override fun onPermissionGranted() {
    }
}
package vn.tapbi.sample2021kotlin.ui.main.cropimage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import com.yalantis.ucrop.UCrop
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
        binding.uCrop.setImageBitmap(bitmap)

        val rect: RectF = RectF(0f, 0f, bitmap.width/2f, bitmap.height/2f)

        binding.tvNext.setOnClickListener {
            binding.uCrop.setCropRect(rect)
            binding.uCrop.setCropBoundsChangeListener {

            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)

            val bundle = Bundle()
            bundle.putString("uriImage", resultUri.toString())
            Navigation.findNavController(binding.root).navigate(R.id.showCutFragment, bundle)
            Navigation.findNavController(binding.root).navigate(R.id.showCutFragment, bundle)
        } else if (resultCode == UCrop.RESULT_ERROR) {

            val cropError = UCrop.getError(data!!)
        }
    }

    override fun onPermissionGranted() {
    }
}
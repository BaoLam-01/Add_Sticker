package vn.tapbi.sample2021kotlin.ui.main.cropimage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.view.GestureCropImageView
import com.yalantis.ucrop.view.OverlayView
import com.yalantis.ucrop.view.UCropView
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentCropImageBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import java.io.File


class CropImageFragment : BaseBindingFragment<FragmentCropImageBinding, CropImageViewModel>() {

    private lateinit var uCrop: UCropView
    private lateinit var mGestureCropImageView: GestureCropImageView
    private lateinit var mOverlayView: OverlayView
    private lateinit var uCropFragment: UCropFragment

    private val choseImage: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                val intent = activityResult.data
                intent?.let {
                    val uri: Uri? = intent.data

                    uri?.let {
                        startCrop(uri)

                    }

                }
            }
        }


    private fun startCrop(uri: Uri) {
        var uCrop = UCrop.of(
            uri,
            Uri.fromFile(
                File(
                    requireContext().cacheDir,
                    SAMPLE_CROPPED_IMAGE_NAME
                )
            )
        )


        uCrop = configUCrop(uCrop)

        uCropFragment = uCrop.fragment

        parentFragmentManager.beginTransaction().add(R.id.frameCut, uCropFragment).commit()

    }

    private fun configUCrop(uCrop: UCrop): UCrop {

        uCrop.withAspectRatio(0.5f, 0.5f)

        val options: UCrop.Options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
//
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(true)



        return uCrop.withOptions(options)
    }


    override fun getViewModel(): Class<CropImageViewModel> {
        return CropImageViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_crop_image

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {

        pickFromGallery()

        evenClick()

    }
    private fun evenClick() {
        binding.tvNext.setOnClickListener {
            uCropFragment.cropAndSaveImage()
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*")
            .addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        choseImage.launch(Intent.createChooser(intent, getString(R.string.select_picture)))
    }

    override fun onPermissionGranted() {
    }
    companion object {
        private const val SAMPLE_CROPPED_IMAGE_NAME: String = "SampleCropImage.png"
    }

}
package vn.tapbi.sample2021kotlin.ui.main.handcutout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentHandCutOutBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import java.io.File
import java.io.FileOutputStream


class HandCutOutFragment : BaseBindingFragment<FragmentHandCutOutBinding, HandCutOutViewModel>(){


    override fun getViewModel(): Class<HandCutOutViewModel> {
        return HandCutOutViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_hand_cut_out

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {
        initView()

    }

    private fun initView() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)
        binding.cutOutView.setImageBitmap(bitmap)

        binding.cutOutView.setOnCutOutListener { isReady ->
            if (isReady) {
                binding.tvNext.visibility = View.VISIBLE
            } else {
                binding.tvNext.visibility = View.INVISIBLE
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context): Uri {
        val file = File(context.cacheDir, "cutOutImage.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file.toUri()
    }


    override fun onPermissionGranted() {
    }

//    fun gomSangCutout() {
//
//        val drawable = ContextCompat.getDrawable(requireContext(),R.drawable.deblured_cutty_fox) as BitmapDrawable
//        val bitmap = drawable.bitmap
//
//        binding.cropView.setOnCropListener { result: Bitmap? ->
//            binding.cropView.setImageBitmap(result)
//        }
//
//        binding.cropView.setMaginfierEnabled(false)
//        binding.cropView.lineWithDashPaint.apply {
//            color = ContextCompat.getColor(requireContext(), R.color.color_paint)
//            style = Style.STROKE
//            isAntiAlias = true
//            pathEffect = null
//
//        }
//
//        binding.cropView.setImageBitmap(bitmap)
//    }
}
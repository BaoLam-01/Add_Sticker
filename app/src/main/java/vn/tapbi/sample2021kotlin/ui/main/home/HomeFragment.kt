package vn.tapbi.sample2021kotlin.ui.main.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import timber.log.Timber
import vn.tapbi.sample2021kotlin.App
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentHomeBinding
import vn.tapbi.sample2021kotlin.feature.stickerview.DrawableSticker
import vn.tapbi.sample2021kotlin.feature.stickerview.Sticker
import vn.tapbi.sample2021kotlin.feature.stickerview.TextSticker
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment


class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {
        initView()
        evenClick()
        initData()
        Timber.e("onCreatedView")
    }

    private fun initData() {
    }

    private fun initView(){

    }

    /*event*/
    private fun evenClick(){
        binding.btnAddSticker.setOnClickListener{
            addSticker()
        }

        binding.btnAddText.setOnClickListener{
            addText()
        }

        binding.btnOnOffStroke.setOnClickListener{
            onOffStroke()
        }


        binding.btnOnOffShadow.setOnClickListener{
            onOffShadow()
        }

    }

    private fun addSticker() {
        val drawable = ContextCompat.getDrawable(requireContext(),R.drawable.beard2)
        binding.stickerView.addSticker(DrawableSticker(drawable),Sticker.Position.BEARD)
    }

    private lateinit var textSticker : TextSticker

    private fun addText() {
            textSticker = TextSticker(requireContext())
        textSticker.setText("Hello Emoji")
        textSticker.setTextColor(Color.BLUE)
        textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)

        val typeFace = ResourcesCompat.getFont(requireContext(),R.font.sf_pro_display_bold)
        val type = Typeface.createFromAsset(App.instance.assets,"fonts/sf_pro_display_bold.ttf")

        textSticker.setTypeface(type)
        textSticker.setStrokeColor(Color.WHITE)
        textSticker.setStrokeWidth(10f)
        textSticker.setStroke(true)

        textSticker.resizeText()
        binding.stickerView.addSticker(textSticker)

    }

    private fun onOffStroke() {

        if (textSticker == null) {
            return
        }

        if (textSticker.stroke) {
            textSticker.setStroke(false)
            binding.stickerView.invalidate()
        } else{
            textSticker.setStroke(true)
            binding.stickerView.invalidate()
        }
    }

    private fun onOffShadow() {

        if (textSticker == null) {
            return
        }

        if (textSticker.shadow) {
            textSticker.setShadowText(false)
            binding.stickerView.invalidate()
        } else{
            textSticker.setShadowText(true)
            binding.stickerView.invalidate()
        }
    }


    override fun observerData() {

    }

    override fun onPermissionGranted() {
    }



}
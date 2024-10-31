package vn.tapbi.sample2021kotlin.ui.main.showcut

import android.net.Uri
import android.os.Bundle
import android.view.View
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentShowCutBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingFragment
import vn.tapbi.sample2021kotlin.utils.BitmapUtils

class ShowCutFragment : BaseBindingFragment<FragmentShowCutBinding, ShowCutViewModel>(){
    override fun getViewModel(): Class<ShowCutViewModel> {
        return ShowCutViewModel::class.java
    }

    override val layoutId: Int
        get() = R.layout.fragment_show_cut

    override fun observerData() {
    }

    override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {

        val uriString = arguments?.getString("uriImage")
        val uri = Uri.parse(uriString)

        val bitmapImage = BitmapUtils.getBitmapFromUri(uri, context)

        binding.showImage.setImageBitmap(bitmapImage)

    }

    override fun onPermissionGranted() {
    }
}
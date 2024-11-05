package vn.tapbi.sample2021kotlin.ui.main.adjustcut

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.FragmentAdjustCutBinding
import vn.tapbi.sample2021kotlin.feature.adjustcut.EraserView
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
        val bm = BitmapFactory.decodeResource(resources, R.drawable.meo)
//        binding.adjustCut.setImageBitmap(bm)
//
//        binding.btnSwap.setOnClickListener {
//            binding.adjustCut.setPaintEraser(!binding.adjustCut.getPaintEraser())
//        }


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meo)


        // Tải và thiết lập hình ảnh
        binding.eraserView.setImage(bitmap)

        // Nút chuyển đổi giữa xóa và khôi phục
        binding.btnSwap.setOnClickListener {
            isErasing = !isErasing
            binding.eraserView.setEraseMode(isErasing)
        }
    }

    override fun onPermissionGranted() {
    }
}
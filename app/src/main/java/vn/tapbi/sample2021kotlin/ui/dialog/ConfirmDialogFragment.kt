package vn.tapbi.sample2021kotlin.ui.dialog

import android.os.Bundle
import android.view.View
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.databinding.DialogConfirmBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingDialogFragment


class ConfirmDialogFragment(var dialogConfirmClickListener: DialogConfirmClickListener?) :
    BaseBindingDialogFragment<DialogConfirmBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_confirm

    protected override fun onCreatedView(view: View?, savedInstanceState: Bundle?) {
        val title: String =
            arguments?.getString(Constant.ARGUMENT_FRAGMENT_MESSAGE_TITLE).toString()
        binding.tvTitleDialog.text = title
        binding.tvYes.setOnClickListener {
            if (dialogConfirmClickListener != null) {
                dialogConfirmClickListener!!.onClickOk()
            }
            dismiss()
        }
        binding.tvNo.setOnClickListener {
            if (dialogConfirmClickListener != null) {
                dialogConfirmClickListener!!.onClickCancel()
            }
            dismiss()
        }
    }

    interface DialogConfirmClickListener {
        fun onClickOk()
        fun onClickCancel()
    }

    companion object {
        fun newInstance(
            title: String?,
            dialogConfirmClickListener: DialogConfirmClickListener?
        ): ConfirmDialogFragment {
            val frag = ConfirmDialogFragment(dialogConfirmClickListener)
            val args = Bundle()
            args.putString(Constant.ARGUMENT_FRAGMENT_MESSAGE_TITLE, title)
            frag.arguments = args
            return frag
        }
    }

}

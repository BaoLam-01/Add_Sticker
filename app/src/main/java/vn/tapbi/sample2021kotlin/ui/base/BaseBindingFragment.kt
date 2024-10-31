package vn.tapbi.sample2021kotlin.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import vn.tapbi.sample2021kotlin.ui.main.MainViewModel

abstract class BaseBindingFragment<B : ViewDataBinding, T : BaseViewModel> :
    BaseFragment() {
    lateinit var binding: B
    lateinit var viewModel: T
    lateinit var mainViewModel: MainViewModel
    protected abstract fun getViewModel(): Class<T>
    abstract val layoutId: Int
    private var loaded = false
    private var lastClickTime: Long = 0

    protected abstract fun observerData()
    protected abstract fun onCreatedView(view: View?, savedInstanceState: Bundle?)
    protected abstract fun onPermissionGranted()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (needInsetTop()) {
            var insectTop = 0
            if (requireActivity() is BaseActivity) {
                insectTop = (requireActivity() as BaseActivity).statusBarHeight
            }
            binding.root.setPadding(
                binding.root.paddingLeft, insectTop, binding.root.paddingRight,
                binding.root.paddingBottom
            )
        }
        viewModel = ViewModelProvider(this)[getViewModel()]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (!needToKeepView()) {
            onCreatedView(view, savedInstanceState)
            loaded = true
        } else {
            if (!loaded) {
                onCreatedView(view, savedInstanceState)
                loaded = true
            }
        }
        observerData()
    }

    open fun needInsetTop(): Boolean {
        return true
    }

    open fun needToKeepView(): Boolean {
        return false
    }


    override fun onStart() {
        super.onStart()
        lastClickTime = 0
    }

    protected fun checkTime(): Boolean {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < 600) {
            return false
        }
        lastClickTime = currentTime
        return true
    }

}
package vn.tapbi.sample2021kotlin.ui.main

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.common.models.MessageEvent
import vn.tapbi.sample2021kotlin.databinding.ActivityMainBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun getViewModel(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupView(savedInstanceState: Bundle?) {}
    override fun setupData() {

    }

    private fun onPermissionGranted() {
        initData()
    }

    private fun initData() {
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}

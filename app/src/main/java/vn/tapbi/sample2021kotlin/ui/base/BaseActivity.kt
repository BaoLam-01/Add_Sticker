package vn.tapbi.sample2021kotlin.ui.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import vn.tapbi.sample2021kotlin.data.local.SharedPreferenceHelper
import vn.tapbi.sample2021kotlin.utils.LocaleUtils
import vn.tapbi.sample2021kotlin.utils.Utils
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var mPrefHelper: SharedPreferenceHelper
    var statusBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleUtils.applyLocale(this)
        super.onCreate(savedInstanceState)
        statusBarHeight = Utils.getStatusBarHeight(this)
    }


    override fun attachBaseContext(newBase: Context) {
        LocaleUtils.applyLocale(newBase)
        super.attachBaseContext(newBase)
    }

    open fun changeSystemUIColor(
        colorStatusBar: Int,
        colorNavigationBar: Int,
        darkStatusBar: Boolean,
        darkNavigation: Boolean
    ) {
        val systemUiScrim = Color.parseColor("#40000000")
        var systemUiVisibility = 0
        // Use a dark scrim by default since light status is API 23+
        val winParams = window.attributes
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (darkNavigation) {
                window.navigationBarColor = colorNavigationBar
            } else {
                window.navigationBarColor = systemUiScrim
            }
            if (darkStatusBar) {
                window.statusBarColor = colorStatusBar
            } else {
                window.statusBarColor = systemUiScrim
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!darkStatusBar) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = colorStatusBar
            if (darkNavigation) {
                window.navigationBarColor = colorNavigationBar
            } else {
                window.navigationBarColor = systemUiScrim
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!darkNavigation) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            window.navigationBarColor = colorNavigationBar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
        systemUiVisibility = systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        window.decorView.systemUiVisibility = systemUiVisibility
        window.attributes = winParams
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}

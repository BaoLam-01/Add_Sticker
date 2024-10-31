package vn.tapbi.sample2021kotlin

import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.data.local.SharedPreferenceHelper
import vn.tapbi.sample2021kotlin.utils.LocaleUtils
import vn.tapbi.sample2021kotlin.utils.MyDebugTree
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class App : MultiDexApplication() {
    @Inject
    lateinit var mPrefHelper: SharedPreferenceHelper

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RxJavaPlugins.setErrorHandler { t: Throwable? ->
            Timber.w(
                t
            )
        }
        initLog()
        var newUsing = mPrefHelper.getInt(Constant.NEW_USING)
        if (newUsing == 0) {
            newUsing = 1
            mPrefHelper.storeInt(Constant.NEW_USING, newUsing)
            for (i in LocaleUtils.getLanguages().indices) {
                val newLocale = Locale(LocaleUtils.getLanguages()[i].codeLocale)
                if (newLocale.language.contains(LocaleUtils.getLocaleCompat(baseContext.resources).language)) {
                    mPrefHelper.storeString(
                        Constant.PREF_SETTING_LANGUAGE, LocaleUtils.getLanguages(
                        )[i].codeLocale
                    )
                    break
                }
            }
        }
    }


    private fun initLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.applyLocale(this)
    }

}

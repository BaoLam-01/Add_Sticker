package vn.tapbi.sample2021kotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import vn.tapbi.sample2021kotlin.App
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.ui.main.MainActivity
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.common.models.Language
import java.util.*


object LocaleUtils {
    var codeLanguageCurrent: String? = Constant.LANGUAGE_EN

    fun applyLocale(context: Context) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
        codeLanguageCurrent =
            preferences.getString(Constant.PREF_SETTING_LANGUAGE, Constant.LANGUAGE_EN)
        if (TextUtils.isEmpty(codeLanguageCurrent)) {
            codeLanguageCurrent = Constant.LANGUAGE_EN
        }
        val newLocale = Locale(codeLanguageCurrent.toString())
        updateResource(context, newLocale)
        if (context !== context.applicationContext) {
            updateResource(context.applicationContext, newLocale)
        }
    }

    fun setLocal(local: String, activity: Context): Context {
        var context: Context = activity
        val locale = Locale(local)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        context = context.createConfigurationContext(config)

        return context
    }

    private fun updateResource(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        val resources = context.resources
        val current = getLocaleCompat(resources)
        if (current === locale) {
            return
        }
        val configuration = Configuration(resources.configuration)
        if (isAtLeastSdkVersion(Build.VERSION_CODES.N)) {
            configuration.setLocale(locale)
        } else if (isAtLeastSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun getLocaleCompat(resources: Resources): Locale {
        val configuration = resources.configuration
        return if (isAtLeastSdkVersion(Build.VERSION_CODES.N)) configuration.locales[0] else configuration.locale
    }

    fun isAtLeastSdkVersion(versionCode: Int): Boolean {
        return Build.VERSION.SDK_INT >= versionCode
    }

    fun applyLocaleAndRestart(activity: Activity, localeString: String) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(activity)
        preferences.edit().putString(Constant.PREF_SETTING_LANGUAGE, localeString).apply()
        applyLocale(activity)
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.startActivity(intent)
        ActivityCompat.finishAffinity(activity)
    }

    fun applyLocalInFirst(activity: Activity, localeString: String?) {
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(activity)
        preferences.edit().putString(Constant.PREF_SETTING_LANGUAGE, localeString).apply()
        applyLocale(activity)
    }

    fun getLanguages(): List<Language> {
        val list: MutableList<Language> = ArrayList()
        list.add(Language(Constant.LANGUAGE_EN, App.instance.resources.getString(R.string.english)))
        list.add(Language(Constant.LANGUAGE_VN, App.instance.resources.getString(R.string.vietnam)))
        return list
    }

    val Context.preferredLocale: Locale
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale
        }
}

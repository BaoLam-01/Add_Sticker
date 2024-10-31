package vn.tapbi.sample2021kotlin.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.SparseArray
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun getTimeString(time: Long): String {
        val date = Date(time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getScreenWidth(context: Context): Int {
        val displaymetrics = context.resources.displayMetrics
        return displaymetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val displaymetrics = context.resources.displayMetrics
        return displaymetrics.heightPixels
    }

    fun isOnline(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun <C> asList(sparseArray: SparseArray<C>?): List<C>? {
        if (sparseArray == null) return null
        val arrayList: MutableList<C> = ArrayList(sparseArray.size())
        for (i in 0 until sparseArray.size()) arrayList.add(sparseArray.valueAt(i))
        return arrayList
    }

    fun pxToDp(px: Float): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    val localeCompat: Locale
        get() {
            val configuration = Resources.getSystem().configuration
            return if (isAtLeastSdkVersion(Build.VERSION_CODES.N)) configuration.locales[0] else configuration.locale
        }

    fun isAtLeastSdkVersion(versionCode: Int): Boolean {
        return Build.VERSION.SDK_INT >= versionCode
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
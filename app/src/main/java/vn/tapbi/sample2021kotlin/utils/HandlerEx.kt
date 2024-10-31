package vn.tapbi.sample2021kotlin.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import timber.log.Timber
import java.lang.Exception

fun safeDelay(delayMillis: Long = 0, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        try {
            action()
        } catch (e: Exception) {
            Timber.e("safeDelay: $e")
        }
    }, delayMillis)
}
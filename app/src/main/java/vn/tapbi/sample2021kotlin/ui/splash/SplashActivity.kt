package vn.tapbi.sample2021kotlin.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.ui.base.BaseActivity
import vn.tapbi.sample2021kotlin.ui.main.MainActivity
import vn.tapbi.sample2021kotlin.utils.safeDelay


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        safeDelay(1000) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
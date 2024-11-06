package vn.tapbi.sample2021kotlin.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import vn.tapbi.sample2021kotlin.R
import vn.tapbi.sample2021kotlin.databinding.ActivityMainBinding
import vn.tapbi.sample2021kotlin.ui.base.BaseBindingActivity
import vn.tapbi.sample2021kotlin.ui.main.showcut.ShowCutFragment

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>(), UCropFragmentCallback {
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

    override fun loadingProgress(showLoader: Boolean) {
    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {
        when(result?.mResultCode){
            RESULT_OK -> {
                handleCropResult(result.mResultData)
            }
            UCrop.RESULT_ERROR ->{
                handleCropError(result.mResultData)
            }
        }
    }

    private fun handleCropResult(result: Intent){
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            val bundle = Bundle()
            bundle.putString("outUri", resultUri.toString())

            supportFragmentManager.beginTransaction().replace(R.id.fragment, ShowCutFragment.newInstance(bundle)).commit()

        } else {
            Toast.makeText(
                this,
                "error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun handleCropError(result: Intent){

    }

}

package vn.tapbi.sample2021kotlin.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.common.LiveEvent
import vn.tapbi.sample2021kotlin.common.models.MessageEvent
import vn.tapbi.sample2021kotlin.ui.base.BaseViewModel
import java.util.ArrayList
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
}

package com.tr4n.puzzle.base

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.BuildConfig
import com.tr4n.puzzle.util.SingleLiveData
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope, Observable, KoinComponent {

    val messageToast = SingleLiveData<Any>()

    val messageSnackBar = SingleLiveData<String>()

    val throwable = SingleLiveData<Throwable>()

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        throwable.value = t
    }

    protected val scope = viewModelScope.plus(exceptionHandler)

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) =
        callbacks.add(callback)

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) =
        callbacks.remove(callback)

    fun notifyChange() = callbacks.notifyCallbacks(this, 0, null)

    fun notifyPropertyChanged(fieldId: Int) = callbacks.notifyCallbacks(this, fieldId, null)

    val loading = MutableLiveData<Boolean>()
    val exception = MutableLiveData<Exception>()

    private var loadingCount = 0

    protected fun <T> launchTaskSync(
        onRequest: suspend CoroutineScope.() -> T,
        onSuccess: (T) -> Unit = {},
        onError: (Exception) -> Unit = ::handleException,
        isShowLoading: Boolean = true
    ) = scope.launch {

        showLoading(isShowLoading)
        try {
            val result = onRequest(this)
            onSuccess(result).apply {
                hideLoading(isShowLoading)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            onError(e).apply {
                hideLoading(isShowLoading)
            }
        }

    }

    protected fun showLoading(isShowLoading: Boolean) {
        if (!isShowLoading) return
        loadingCount++
        if (loading.value != true) loading.value = true
    }

    protected fun hideLoading(isShowLoading: Boolean) {
        if (!isShowLoading) return
        loadingCount--
        if (loadingCount <= 0) {
            // reset loadingCount
            loadingCount = 0
            loading.value = false
        }
    }

    protected open fun handleException(e: Exception) {
        e.printStackTrace()
        throwable.value = e
    }
}

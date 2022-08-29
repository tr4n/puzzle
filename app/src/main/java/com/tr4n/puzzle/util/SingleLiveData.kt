package com.tr4n.puzzle.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * SingleLiveData is a data holder class as the same as MutableLiveData but
 * only one Observer will be notified of changes.
 * Custom mutable live data that used for single event
 *              such as navigation (for configuration change), show toast..
 * @see: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
class SingleLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(
                SingleLiveData::class.java.simpleName,
                "Multiple observers registered but only one will be notified of changes."
            )
            return
        }
        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    /**
     * SingleLiveData for no data input. Make call more clear
     * @Example: Navigation with no data: SingleLiveEvent<Unit>()
     */
    fun call() {
        value = null
    }
}


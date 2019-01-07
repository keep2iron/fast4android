package io.github.keep2iron.android.utilities

import android.os.Looper

import io.reactivex.Observer
import io.reactivex.disposables.Disposables

object Preconditions {

    fun <T> checkNotNull(value: T?, message: String): T {
        if (value == null) {
            throw NullPointerException(message)
        }
        return value
    }

    fun checkMainThread(observer: Observer<*>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().name))
            return false
        }
        return true
    }
}
package io.github.keep2iron.android.core

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/12 14:52
 */
class LifecycleViewModelFactory(private val lifecycleOwner: LifecycleOwner) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getDeclaredConstructor(LifecycleOwner::class.java).newInstance(lifecycleOwner)
    }
}

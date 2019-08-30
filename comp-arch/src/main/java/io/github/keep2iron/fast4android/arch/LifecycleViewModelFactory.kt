package io.github.keep2iron.fast4android.arch

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.keep2iron.fast4android.core.Fast4Android

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/12 14:52
 */
class LifecycleViewModelFactory(private val lifecycleOwner: LifecycleOwner) :
  ViewModelProvider.NewInstanceFactory() {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return try {
      modelClass.getDeclaredConstructor(LifecycleOwner::class.java).newInstance(lifecycleOwner)
    } catch (ex: NoSuchMethodException) {
      modelClass.getDeclaredConstructor(LifecycleOwner::class.java, Context::class.java)
        .newInstance(lifecycleOwner, Fast4Android.CONTEXT)
    }
  }
}

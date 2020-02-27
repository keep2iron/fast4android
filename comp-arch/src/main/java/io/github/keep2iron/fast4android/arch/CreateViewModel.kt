package io.github.keep2iron.fast4android.arch

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KProperty

class CreateViewModel(val lifecycleOwner: LifecycleOwner) {

  inline operator fun <reified T : LifeCycleViewModel> getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
    return ViewModelProviders.of(thisRef, LifecycleViewModelFactory(lifecycleOwner)).get(T::class.java)
  }

  operator fun setValue(thisRef: AppCompatActivity, property: KProperty<*>, model: LifeCycleViewModel) {
    throw IllegalArgumentException("did't be allow to call setValue")
  }

  inline operator fun <reified T : LifeCycleViewModel> getValue(thisRef: androidx.fragment.app.Fragment, property: KProperty<*>): T {
    return ViewModelProviders.of(thisRef, LifecycleViewModelFactory(lifecycleOwner)).get(T::class.java)
  }

  operator fun setValue(thisRef: androidx.fragment.app.Fragment, property: KProperty<*>, model: LifeCycleViewModel) {
    throw IllegalArgumentException("did't be allow to call setValue")
  }

}

fun findViewModel(lifecycleOwner: LifecycleOwner): CreateViewModel {
  return CreateViewModel(lifecycleOwner)
}
package io.github.keep2iron.fast4android.arch.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class LazyLoader private constructor(lifecycle: Lifecycle, lazyLoad: () -> Unit) {

  private var loaded: Boolean = false

  init {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
      override fun onResume(owner: LifecycleOwner) {
        if (!loaded) {
          lazyLoad()
          loaded = true
        }
      }
    })
  }

  companion object {

    fun attach(activity: FragmentActivity, lazyLoad: () -> Unit) {
      this.attach(activity as LifecycleOwner, lazyLoad)
    }

    fun attach(fragment: Fragment, lazyLoad: () -> Unit) {
      attach(fragment.viewLifecycleOwner, lazyLoad)
    }

    fun attach(lifecycleOwner: LifecycleOwner, lazyLoad: () -> Unit) {
      LazyLoader(lifecycleOwner.lifecycle, lazyLoad)
    }
  }

}
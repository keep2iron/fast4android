package io.github.keep2iron.fast4android.arch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.github.keep2iron.fast4android.rx.RxLifecycleDispatcher
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/30 17:55
 */
open class LifeCycleViewModule(application: Application, private val owner: LifecycleOwner) :
  AndroidViewModel(application), RxLifecycleOwner {

  private val lifecycleDispatcher = RxLifecycleDispatcher(owner)

  override val publishSubject: BehaviorSubject<LifecycleEvent> = lifecycleDispatcher.publishSubject
}
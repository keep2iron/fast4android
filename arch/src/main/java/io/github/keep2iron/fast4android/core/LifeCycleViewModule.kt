package io.github.keep2iron.fast4android.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/30 17:55
 */
open class LifeCycleViewModule(application: Application, protected val owner: LifecycleOwner) :
  AndroidViewModel(application), RxLifecycleOwner {
  override val publishSubject: BehaviorSubject<LifecycleEvent> = BehaviorSubject.create()

  init {
    owner.lifecycle.addObserver(object : LifecycleObserver {
      @OnLifecycleEvent(Lifecycle.Event.ON_START)
      fun onStart() {
        publishSubject.onNext(LifecycleEvent.START)
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
      fun onResume() {
        publishSubject.onNext(LifecycleEvent.RESUME)
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
      fun onPause() {
        publishSubject.onNext(LifecycleEvent.PAUSE)
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
      fun onStop() {
        publishSubject.onNext(LifecycleEvent.STOP)
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
      fun onDestroy() {
        publishSubject.onNext(LifecycleEvent.DESTROY)
        owner.lifecycle.removeObserver(this)
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
      fun onCreate() {
        publishSubject.onNext(LifecycleEvent.CREATE)
      }
    })
  }
}
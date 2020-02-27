package io.github.keep2iron.fast4android.arch.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.subjects.BehaviorSubject

class RxLifecycleDispatcher(owner: LifecycleOwner, val publishSubject: BehaviorSubject<LifecycleEvent> = BehaviorSubject.create()) : LifecycleObserver {


  init {
    owner.lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    publishSubject.onNext(LifecycleEvent.CREATE)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    publishSubject.onNext(LifecycleEvent.START)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
    publishSubject.onNext(LifecycleEvent.RESUME)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    publishSubject.onNext(LifecycleEvent.STOP)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    publishSubject.onNext(LifecycleEvent.DESTROY)
  }

}
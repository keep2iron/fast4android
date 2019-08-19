package io.github.keep2iron.fast4android.arch

import io.github.keep2iron.fast4android.ext.ioAsyncScheduler
import io.github.keep2iron.fast4android.rx.LifecycleEvent
import io.github.keep2iron.fast4android.rx.RxLifecycle
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

interface RxLifecycleOwner {

  val publishSubject: BehaviorSubject<LifecycleEvent>

  fun <T> observableIoAsyncAndBindLifecycle(): FlowableTransformer<T, T> {
    return FlowableTransformer { upstream ->
      upstream.ioAsyncScheduler()
        .compose(bindFlowableLifeCycle())
    }
  }

  fun <T> flowableIoAsyncAndBindLifecycle(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.ioAsyncScheduler()
        .compose(bindObservableLifeCycle())
    }
  }

  fun <T> bindObservableLifeCycle(): ObservableTransformer<T, T> {
    return RxLifecycle.bindUntilEvent(publishSubject, LifecycleEvent.DESTROY)
  }

  fun <T> bindFlowableLifeCycle(): FlowableTransformer<T, T> {
    return RxLifecycle.bindUntilEvent(publishSubject, LifecycleEvent.DESTROY)
  }

}
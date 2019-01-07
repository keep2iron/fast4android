package io.github.keep2iron.android.ext

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent

import io.github.keep2iron.android.rx.LifecycleEvent
import io.github.keep2iron.android.rx.RxLifecycle
import io.github.keep2iron.android.utilities.RxTransUtil
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/30 17:55
 */
open class LifeCycleViewModule(application: Application, protected val owner: LifecycleOwner) : AndroidViewModel(application), LifecycleObserver {
    private val mSubject = BehaviorSubject.create<LifecycleEvent>()

    protected val context = application

    /**
     * 对外界提供可订阅的生命周期观测对象
     */
    fun getLifeCyclerEvent(): BehaviorSubject<LifecycleEvent> {
        return mSubject
    }

    init {
        owner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                mSubject.onNext(LifecycleEvent.START)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                mSubject.onNext(LifecycleEvent.RESUME)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                mSubject.onNext(LifecycleEvent.PAUSE)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                mSubject.onNext(LifecycleEvent.STOP)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mSubject.onNext(LifecycleEvent.DESTROY)
                owner.lifecycle.removeObserver(this)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                mSubject.onNext(LifecycleEvent.CREATE)
            }
        })
    }

    /**
     * 提供Flowable绑定生命周期并进行线程异步
     */
    fun <T> flowableBindLifecycleWithSwitchSchedule(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxFlowableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(mSubject, LifecycleEvent.DESTROY))
        }
    }

    /**
     * 提供Observable绑定生命周期并进行线程异步
     */
    fun <T> observableBindLifecycleWithSwitchSchedule(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxObservableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(mSubject, LifecycleEvent.DESTROY))
        }
    }
}
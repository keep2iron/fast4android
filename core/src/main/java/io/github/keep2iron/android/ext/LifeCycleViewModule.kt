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
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/30 17:55
 */
open class LifeCycleViewModule(application: Application, private val owner: LifecycleOwner) : AndroidViewModel(application), LifecycleObserver {
    private val mSubject = BehaviorSubject.create<LifecycleEvent>()

    protected val context = application

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

    fun <T> bindFlowableLifeCycle(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxFlowableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(mSubject, LifecycleEvent.DESTROY))
        }
    }

    /**
     * 绑定让订阅进行绑定生命周期
     */
    fun <T> bindObservableLifeCycle(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.compose(RxTransUtil.rxObservableScheduler())
                    .compose(RxLifecycle.bindUntilEvent(mSubject, LifecycleEvent.DESTROY))
        }
    }
}
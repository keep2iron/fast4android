package io.github.keep2iron.fast4android.arch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import io.github.keep2iron.fast4android.arch.rx.LifecycleEvent
import io.github.keep2iron.fast4android.arch.rx.RxLifecycleDispatcher
import io.github.keep2iron.base.Fast4Android
import io.reactivex.subjects.BehaviorSubject

open class LifeCycleViewModel(owner: LifecycleOwner) :
        AndroidViewModel(Fast4Android.CONTEXT as Application), RxLifecycleOwner {

    final override val publishSubject: BehaviorSubject<LifecycleEvent> = BehaviorSubject.create()

    private val lifecycleDispatcher = RxLifecycleDispatcher(owner, publishSubject)

}
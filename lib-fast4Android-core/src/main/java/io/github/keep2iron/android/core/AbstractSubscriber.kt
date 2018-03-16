/*
 * Create bt Keep2iron on 17-6-22 下午4:01
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.core

import android.util.Log

import com.orhanobut.logger.Logger

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author keep2iron
 * @date 2017/2/14
 *
 *
 * 请在application中初始化这个方法
 */
abstract class AbstractSubscriber<T> : Observer<T>, Subscriber<T> {
    private var disposable: Disposable? = null
    private var subscription: Subscription? = null

    override fun onComplete() {}

    override fun onSubscribe(disposable: Disposable) {
        this.disposable = disposable
    }

    override fun onError(throwable: Throwable) {
        Logger.e(Log.getStackTraceString(throwable))
    }


    override fun onNext(t: T) {
        onSuccess(t)
    }

    /**
     * 当正常请求时进行调用
     *
     * @param resp
     */
    abstract fun onSuccess(resp: T)

    override fun onSubscribe(subscription: Subscription) {
        this.subscription = subscription
    }

    fun cancel() {
        if (!disposable!!.isDisposed) {
            disposable?.dispose()
        }

        subscription!!.cancel()
    }
}
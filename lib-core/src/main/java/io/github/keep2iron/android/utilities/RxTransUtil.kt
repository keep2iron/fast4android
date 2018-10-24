package io.github.keep2iron.android.utilities

import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by 薛世君
 * Date : 2016/11/11
 * Email : 497881309@qq.com
 *
 *
 * subscribeOn()改变调用它之前代码的线程
 * observeOn()改变调用它之后代码的线程
 */

object RxTransUtil {
    private val sObservableTransformer = ObservableTransformer<Any, Any> { observable ->
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    private val sFlowableTransformer = FlowableTransformer<Any, Any> { upstream ->
        upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun <T> rxObservableScheduler(): ObservableTransformer<T, T> {
        return sObservableTransformer as ObservableTransformer<T, T>
    }

    fun <T> rxFlowableScheduler(): FlowableTransformer<T, T> {
        return sFlowableTransformer as FlowableTransformer<T, T>
    }
}
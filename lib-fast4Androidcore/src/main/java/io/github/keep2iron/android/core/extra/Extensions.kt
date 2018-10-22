package io.github.keep2iron.android.core.extra

import android.app.Activity
import android.support.annotation.DimenRes
import android.util.Log
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.BuildConfig
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.utilities.RxTransUtil
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/01 17:34
 */

fun <T> AbstractActivity<*>.rxObservableScheduler(): ObservableTransformer<T, T> {
    return RxTransUtil.rxObservableScheduler()
}

fun <T> AbstractActivity<*>.bindFlowableLifeCycle(): FlowableTransformer<T, T> {
    return RxTransUtil.rxFlowableScheduler()
}

fun View.dp2px(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp * density).toInt()
}

fun View.px2dp(px: Int): Int {
    val density = resources.displayMetrics.density
    return (px / density).toInt()
}

fun View.dimen(@DimenRes resId: Int): Float {
    return context.resources.getDimension(resId)
}


const val TAG: String = "fast4android"

fun Activity.d(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, message)
    }
}

fun Activity.e(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message)
    }
}

fun Activity.i(message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(TAG, message)
    }
}


fun Activity.v(message: String) {
    if (BuildConfig.DEBUG) {
        Log.v(TAG, message)
    }
}

/**
 * 注册adapter
 * @param adapters 各种类型的adapter
 */
fun DelegateAdapter.register(vararg adapters: DelegateAdapter.Adapter<RecyclerViewHolder>) {
    setAdapters(adapters.toList())
}


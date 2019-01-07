package io.github.keep2iron.android.ext

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.keep2iron.android.ComponentServiceProvider
import io.github.keep2iron.android.Fast4Android
import io.github.keep2iron.android.rx.LifecycleEvent
import io.github.keep2iron.android.rx.RxLifecycle
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 用于扩展 Application 的扩展方法类
 */
val COMPONENT_SERVICE = ArrayMap<String, Any>()

/**************************************** Context扩展 **************************************************/
/**
 * 注册组件服务
 */
fun <T> Context.registerComponentService(vararg providers: ComponentServiceProvider<T>) {
    providers.forEach {
        COMPONENT_SERVICE[it.componentName] = it.provideComponentService(this.applicationContext as Application)
    }
}

/**
 * 获取组件
 */
fun <T> Context.getComponentService(service: String): T {
    return COMPONENT_SERVICE[service] as T
}

/****************************************** Any扩展 ********************************************/
fun Any.layout(@LayoutRes layoutId: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(Fast4Android.CONTEXT).inflate(layoutId, parent)
}

fun Any.dp2px(dp: Int): Int {
    val density = Fast4Android.CONTEXT.resources.displayMetrics.density
    return (dp * density).toInt()
}

fun Any.px2dp(px: Int): Int {
    val density = Fast4Android.CONTEXT.resources.displayMetrics.density
    return (px / density).toInt()
}

fun Any.sp(sp: Int): Float {
    val scaledDensity = Fast4Android.CONTEXT.resources.displayMetrics.scaledDensity
    return sp * scaledDensity
}

/******************************************** rxjava2扩展 ******************************************************/
fun <T> Observable<T>.switchSchedule(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.switchFlowable(): Flowable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}


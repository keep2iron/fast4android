package io.github.keep2iron.fast4android.ext

import android.app.Application
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.keep2iron.fast4android.ComponentServiceProvider
import io.github.keep2iron.fast4android.Fast4Android

/**
 * 用于扩展 Application 的扩展方法类
 */
val COMPONENT_SERVICE = androidx.collection.ArrayMap<Class<*>, Any>()

/**
 * 注册组件服务
 */
fun <T> registerComponentService(vararg providers: ComponentServiceProvider<T>) {
  providers.forEach {
    COMPONENT_SERVICE[it.providerComponentServiceClass()] =
      it.provideComponentService(Fast4Android.CONTEXT.applicationContext as Application)
  }
}

/**
 * 获取组件
 */
fun <T> getComponentService(clazz: Class<T>): T {
  return COMPONENT_SERVICE[clazz] as T
}

fun layout(@LayoutRes layoutId: Int, parent: ViewGroup? = null): View {
  return LayoutInflater.from(Fast4Android.CONTEXT).inflate(layoutId, parent)
}

fun dp2px(dp: Int): Int {
  val density = Fast4Android.CONTEXT.resources.displayMetrics.density
  return (dp * density).toInt()
}

fun px2dp(px: Int): Int {
  val density = Fast4Android.CONTEXT.resources.displayMetrics.density
  return (px / density).toInt()
}

fun sp(sp: Int): Float {
  val scaledDensity = Fast4Android.CONTEXT.resources.displayMetrics.scaledDensity
  return sp * scaledDensity
}

fun toast() {

}

package io.github.keep2iron.fast4android.ext

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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
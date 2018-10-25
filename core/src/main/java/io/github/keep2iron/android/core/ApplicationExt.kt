package io.github.keep2iron.android.core

import android.app.Application
import android.support.v4.util.ArrayMap

/**
 * 用于扩展 Application 的扩展方法类
 */
val COMPONENT_SERVICE = ArrayMap<String, Any>()

/**
 * 注册组件
 */
fun Application.registerComponentService(service: String, obj: Any) {
    COMPONENT_SERVICE[service] = obj
}

/**
 * 获取组件
 */
fun <T> Application.getComponentService(service: String): T {
    return COMPONENT_SERVICE[service] as T
}
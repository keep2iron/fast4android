package io.github.keep2iron.android.ext

import android.app.Application
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.keep2iron.android.ComponentPackage
import io.github.keep2iron.android.ComponentServiceProvider

/**
 * 用于扩展 Application 的扩展方法类
 */
val COMPONENT_SERVICE = ArrayMap<String, Any>()

fun Application.init(mainComponent: MainComponent) {
    val compPackage = mainComponent.createComponentPackage()

    compPackage.createComponentModuleProvider().forEach {
        it.createComponentModule(this)
    }
    compPackage.createComponentServiceProvider().forEach {
        this.registerComponentService(it)
    }
}

/**
 * 注册组件
 */
private fun <T> Application.registerComponentService(vararg providers: ComponentServiceProvider<T>) {
    providers.forEach {
        COMPONENT_SERVICE[it.componentName] = it.provideComponentService(this)
    }
}

/**
 * 获取组件
 */
fun <T> Application.getComponentService(service: String): T {
    return COMPONENT_SERVICE[service] as T
}

fun layout(context: Context, @LayoutRes layoutId: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(context).inflate(layoutId, parent)
}

fun View.dp2px(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp * density).toInt()
}

fun View.px2dp(px: Int): Int {
    val density = resources.displayMetrics.density
    return (px / density).toInt()
}

fun View.sp(sp: Int): Float {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return sp * scaledDensity
}

/**
 * 用于Application实现该接口
 */
interface MainComponent {

    fun createComponentPackage(): ComponentPackage

}
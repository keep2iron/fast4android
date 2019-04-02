package io.github.keep2iron.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.github.keep2iron.android.ext.registerComponentService

/**
 * 初始化类
 */
@SuppressLint("StaticFieldLeak")
object Fast4Android {

    @JvmStatic
    lateinit var CONTEXT: Context

    fun init(mainComponent: MainComponent, applicationContext: Context) {
        this.CONTEXT = applicationContext.applicationContext

        mainComponent.createComponentModuleProvider().forEach {
            it.createComponentModule(applicationContext as Application)
        }
        mainComponent.createComponentServiceProvider().forEach {
            registerComponentService(it)
        }
    }
}
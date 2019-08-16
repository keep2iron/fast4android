package io.github.keep2iron.fast4android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.github.keep2iron.fast4android.ext.registerComponentService

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
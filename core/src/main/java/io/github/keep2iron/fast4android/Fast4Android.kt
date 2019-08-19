package io.github.keep2iron.fast4android

import android.annotation.SuppressLint
import android.content.Context
import io.github.keep2iron.fast4android.FastLogger.FastLogDelegate

@SuppressLint("StaticFieldLeak")
object Fast4Android {

  lateinit var CONTEXT: Context

  fun logger(logger: FastLogDelegate) {
    FastLogger.setDelegete(logger)
  }

  inline fun init(applicationContext: Context, block: Fast4Android.() -> Unit) {
    this.CONTEXT = applicationContext.applicationContext
    apply(block)
  }
}
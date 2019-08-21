package io.github.keep2iron.fast4android.core

import android.annotation.SuppressLint
import android.content.Context
import io.github.keep2iron.fast4android.core.FastLogger.FastLogDelegate

@SuppressLint("StaticFieldLeak")
object Fast4Android {

  lateinit var CONTEXT: Context

  fun logger(logger: FastLogDelegate) {
    FastLogger.setDelegete(logger)
  }

  fun init(applicationContext: Context) {
    CONTEXT = applicationContext.applicationContext
  }

  inline fun init(applicationContext: Context, block: Fast4Android.() -> Unit) {
    CONTEXT = applicationContext.applicationContext
    apply(block)
  }
}
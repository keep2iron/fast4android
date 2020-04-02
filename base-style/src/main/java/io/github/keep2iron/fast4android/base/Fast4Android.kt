package io.github.keep2iron.fast4android.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.github.keep2iron.fast4android.base.FastLogger.FastLogDelegate

@SuppressLint("StaticFieldLeak")
object Fast4Android {

  lateinit var CONTEXT: Context

  val APPLICATION: Application
    get() {
      return CONTEXT as Application
    }

  fun logger(logger: FastLogDelegate) {
    FastLogger.setDelegate(logger)
  }

  internal fun init(applicationContext: Context) {
    CONTEXT = applicationContext.applicationContext
  }
}
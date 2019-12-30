package io.github.keep2iron.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.github.keep2iron.base.FastLogger.FastLogDelegate

@SuppressLint("StaticFieldLeak")
object Fast4Android {

    lateinit var CONTEXT: Context

    fun logger(logger: FastLogDelegate) {
        FastLogger.setDelegate(logger)
    }

    fun init(applicationContext: Context) {
        CONTEXT = applicationContext.applicationContext
    }

    inline fun init(applicationContext: Context, block: Fast4Android.() -> Unit) {
        CONTEXT = applicationContext.applicationContext
        apply(block)
    }

    fun applicationInitTask(applicationInitTask: ApplicationInitTask) {
        applicationInitTask.onApplicationCreate(CONTEXT as Application)
    }
}
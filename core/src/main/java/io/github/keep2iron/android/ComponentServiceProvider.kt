package io.github.keep2iron.android

import android.app.Application

interface ComponentServiceProvider<T> {

    fun provideComponentService(application: Application): T

    val componentName: String

}
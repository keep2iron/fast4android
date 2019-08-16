package io.github.keep2iron.fast4android

import android.app.Application

/**
 * 用于业务组件暴露
 */
interface ComponentServiceProvider<T> {

  fun provideComponentService(application: Application): T?

  fun providerComponentServiceClass(): Class<T>

}
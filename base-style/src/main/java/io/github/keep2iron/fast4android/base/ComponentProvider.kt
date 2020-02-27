package io.github.keep2iron.fast4android.base

import android.app.Application

/**
 * 用于业务组件暴露
 */
interface ComponentProvider<T> {

  fun onCreateComponent(application: Application): T

}
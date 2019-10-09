package io.github.keep2iron.fast4android.core

import android.app.Application

/**
 * 用于业务组件暴露
 */
interface ComponentProvider<T> {

    fun onCreateComponent(application: Application): T

}
package io.github.keep2iron.android

import android.app.Application

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/29
 *
 * 用于初始化各种各样的第三方模块
 */
interface ComponentModuleProvider{

    fun createComponentModule(application: Application)

}
package io.github.keep2iron.fast4android.base

import android.app.Application

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/29
 *
 * 用于初始化任务
 */
interface ApplicationInitTask {
  fun onApplicationCreate(application: Application)
}
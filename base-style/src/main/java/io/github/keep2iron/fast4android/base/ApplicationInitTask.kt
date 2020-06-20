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

  /**
   * application init method
   *
   * @param application Application object
   */
  fun onApplicationCreate(application: Application)

  /**
   * isMainProcess when in a multiple process app, [onApplicationCreate] will be called many times
   * you can judge this flag only in MainProcess do something.
   *
   * @return if true [onApplicationCreate] only call in main process else [onApplicationCreate] will be called in every process.
   *
   */
  fun initInMainProcess(): Boolean = false

}
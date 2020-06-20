package io.github.keep2iron.fast4android.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Process
import io.github.keep2iron.fast4android.base.FastLogger.FastLogDelegate
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

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

  fun init(applicationContext: Context) {
    CONTEXT = applicationContext.applicationContext

    CONTEXT = CONTEXT.applicationContext

    val processName = getProcessName()

    initTasks.forEach { task ->
      val initInMainProcess = task.initInMainProcess()
      if (initInMainProcess && processName != null && processName == CONTEXT.packageName) {
        task.onApplicationCreate(CONTEXT.applicationContext as Application)
      } else {
        task.onApplicationCreate(CONTEXT.applicationContext as Application)
      }
    }

  }

  private fun getProcessName(): String? {
    return try {
      val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
      val mBufferedReader = BufferedReader(FileReader(file))
      val processName: String = mBufferedReader.readLine().trim()
      mBufferedReader.close()
      processName
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }


  private val _initTasks = arrayListOf<ApplicationInitTask>()
  val initTasks: List<ApplicationInitTask> = _initTasks

  /**
   * register some application init code black.
   *
   * @param applicationTask init task
   */
  fun registerApplicationInitTask(applicationTask: ApplicationInitTask) {
    _initTasks.add(applicationTask)
  }

}
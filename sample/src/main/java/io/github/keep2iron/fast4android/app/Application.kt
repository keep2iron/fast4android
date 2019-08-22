package io.github.keep2iron.fast4android.app

import androidx.multidex.MultiDexApplication
import io.github.keep2iron.fast4android.core.DefaultLogger
import io.github.keep2iron.fast4android.core.Fast4Android

//import io.github.keep2iron.fast4android.launch.ImageLoaderServiceProvider
//import io.github.keep2iron.fast4android.launch.LoggerModule
//import io.github.keep2iron.fast4android.launch.NetworkServiceProvider
//import io.github.keep2iron.fast4android.launch.RefreshLayoutModule
//import io.github.keep2iron.fast4android.launch.ScreenDensityModule

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    Fast4Android.init(this) {
      logger(DefaultLogger())
    }
  }
}
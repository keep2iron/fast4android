package io.github.keep2iron.app

import androidx.multidex.MultiDexApplication
import io.github.keep2iron.app.launch.ImageLoaderServiceProvider
import io.github.keep2iron.app.launch.LoggerModule
import io.github.keep2iron.app.launch.NetworkServiceProvider
import io.github.keep2iron.app.launch.RefreshLayoutModule
import io.github.keep2iron.app.launch.ScreenDensityModule
import io.github.keep2iron.fast4android.ComponentModuleProvider
import io.github.keep2iron.fast4android.ComponentServiceProvider
import io.github.keep2iron.fast4android.Fast4Android
import io.github.keep2iron.fast4android.MainComponent

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : MultiDexApplication(), MainComponent {
  override fun createComponentServiceProvider(): List<ComponentServiceProvider<*>> {
    return listOf(
      ImageLoaderServiceProvider(),
      NetworkServiceProvider()
    )
  }

  override fun createComponentModuleProvider(): List<ComponentModuleProvider> {
    return listOf(
      LoggerModule(),
      RefreshLayoutModule(),
      ScreenDensityModule()
    )
  }

  override fun onCreate() {
    super.onCreate()

    Fast4Android.init(this, this)
  }
}
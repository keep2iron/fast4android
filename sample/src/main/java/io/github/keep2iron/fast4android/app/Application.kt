package io.github.keep2iron.fast4android.app

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import io.github.keep2iron.fast4android.base.ApplicationInitTask
import io.github.keep2iron.fast4android.base.DefaultLogger
import io.github.keep2iron.fast4android.base.Fast4Android
import io.github.keep2iron.pineapple.ImageLoaderManager

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    Fast4Android.logger(DefaultLogger())
    Fast4Android.registerApplicationInitTask(object : ApplicationInitTask {
      override fun onApplicationCreate(application: Application) {
        ImageLoaderManager.init(this@Application)
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance())
      }
    })
    Fast4Android.init(this)
  }
}
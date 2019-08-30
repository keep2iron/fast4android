package io.github.keep2iron.fast4android.arch.swipe

import android.app.Application
import io.github.keep2iron.fast4android.core.ApplicationInitTask

class ParallaxBackApplicationTask : ApplicationInitTask {
  override fun onApplicationCreate(application: Application) {
    application.registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
  }
}
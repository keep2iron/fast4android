package io.github.keep2iron.app.launch

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.github.keep2iron.fast4android.ComponentModuleProvider

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/30
 */
class LoggerModule : ComponentModuleProvider {

  override fun createComponentModule(application: Application) {
    Logger.addLogAdapter(AndroidLogAdapter())
  }
}
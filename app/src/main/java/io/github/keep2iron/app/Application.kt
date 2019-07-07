package io.github.keep2iron.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.github.keep2iron.android.*
import io.github.keep2iron.app.launch.*


/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : MultiDexApplication(), MainComponent {
    override fun createComponentServiceProvider(): List<ComponentServiceProvider<*>> {
        return listOf(ImageLoaderServiceProvider(),
                NetworkServiceProvider())
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
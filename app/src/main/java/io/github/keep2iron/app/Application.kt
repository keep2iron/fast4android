package io.github.keep2iron.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.github.keep2iron.android.*


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
                TencentX5Module(),
                ScreenDensityModule()
        )
    }

    companion object {
        lateinit var instance: Application
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Application.instance = this

        Fast4Android.init(this, this)
    }
}
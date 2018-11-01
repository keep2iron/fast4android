package io.github.keep2iron.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.github.keep2iron.android.ComponentModuleProvider
import io.github.keep2iron.android.ComponentPackage
import io.github.keep2iron.android.ComponentServiceProvider
import io.github.keep2iron.android.ext.MainComponent
import io.github.keep2iron.android.ext.init


/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : MultiDexApplication(), MainComponent {
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

        init(this)
    }

    override fun createComponentPackage(): ComponentPackage {
        return object : ComponentPackage {
            override fun createComponentServiceProvider(): List<ComponentServiceProvider<*>> {
                return listOf(ImageLoaderServiceProvider(),
                        NetworkServiceProvider())
            }

            override fun createComponentModuleProvider(): List<ComponentModuleProvider> {
                return listOf(LoggerModule(),
                        RefreshLayoutModule(),
                        TencentX5Module())
            }
        }
    }


}
package io.github.keep2iron.app.launch

import android.app.Application
import io.github.keep2iron.fast4android.ComponentServiceProvider
import io.github.keep2iron.pomelo.NetworkManager
import io.github.keep2iron.pomelo.convert.CustomGsonConvertFactory
import io.github.keep2iron.pomelo.interceptor.AddCookiesInterceptor
import io.github.keep2iron.pomelo.interceptor.ReceivedCookiesInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/29
 */
class NetworkServiceProvider : ComponentServiceProvider<NetworkManager> {

  override fun providerComponentServiceClass(): Class<NetworkManager> = NetworkManager::class.java

  override fun provideComponentService(application: Application): NetworkManager {

    NetworkManager.init("http://10.0.2.2:8080/") {
      initOkHttp {
        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        addInterceptor(ReceivedCookiesInterceptor())
        addInterceptor(AddCookiesInterceptor())
      }

      initRetrofit {
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        addConverterFactory(CustomGsonConvertFactory.create())
      }
    }

    return NetworkManager.getInstance()
  }
}
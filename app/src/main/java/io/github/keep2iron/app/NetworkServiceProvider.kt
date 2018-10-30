package io.github.keep2iron.app

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.github.keep2iron.android.ComponentServiceProvider
import io.github.keep2iron.app.model.BaseResponse
import io.github.keep2iron.pomelo.NetworkManager
import io.github.keep2iron.pomelo.interceptor.AddCookiesInterceptor
import io.github.keep2iron.pomelo.interceptor.HttpLogger
import io.github.keep2iron.pomelo.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/29
 */
class NetworkServiceProvider : ComponentServiceProvider<NetworkManager> {

    override fun provideComponentService(application: Application): NetworkManager {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLogger())
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        builder.addInterceptor(ReceivedCookiesInterceptor())
        builder.addInterceptor(AddCookiesInterceptor())

        return NetworkManager.Builder("http://192.168.2.169:8080/")
                .setBaseServerResponse(BaseResponse::class.java)
                .build(builder.build())
    }

    override val componentName: String = "network"
}
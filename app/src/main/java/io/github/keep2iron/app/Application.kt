package io.github.keep2iron.app

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.android.net.NetworkManager
import io.github.keep2iron.android.net.interceptor.AddCookiesInterceptor
import io.github.keep2iron.android.net.interceptor.HttpLogger
import io.github.keep2iron.android.net.interceptor.ReceivedCookiesInterceptor
import io.github.keep2iron.app.data.remote.ApiService
import io.github.keep2iron.app.entity.BaseResponse
import io.github.keep2iron.app.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */

class Application : AbstractApplication() {
    override fun initRegisterComponent() {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLogger())
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addNetworkInterceptor(StethoInterceptor())
        builder.addInterceptor(ReceivedCookiesInterceptor())
        builder.addInterceptor(AddCookiesInterceptor())

        val networkManager = NetworkManager.Builder("http://10.0.2.2:8080/")
                .setBaseServerResponse(BaseResponse::class.java)
                .build(builder.build())

        setTag(Constant.NETWORK_MANAGER_KEY, networkManager)

        val apiService = networkManager.getService(ApiService::class.java)
        setTag(Constant.API_SERVICE_KEY, apiService)
    }
}

fun AbstractApplication.getNetworkManager(): NetworkManager {
    return getTag(Constant.NETWORK_MANAGER_KEY) as NetworkManager
}

fun AbstractApplication.getApiService(): ApiService {
    return getTag(Constant.API_SERVICE_KEY) as ApiService
}
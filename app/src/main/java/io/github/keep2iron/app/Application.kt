package io.github.keep2iron.app

import android.content.Context
import android.support.multidex.MultiDex
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.app.model.BaseResponse
import io.github.keep2iron.app.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater
import io.github.keep2iron.imageloader.ImageLoaderManager
import io.github.keep2iron.network.NetworkManager
import io.github.keep2iron.network.interceptor.AddCookiesInterceptor
import io.github.keep2iron.network.interceptor.HttpLogger
import io.github.keep2iron.network.interceptor.ReceivedCookiesInterceptor


/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/07 11:37
 */
class Application : AbstractApplication() {

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun initRegisterComponent() {
        ImageLoaderManager.getImageLoader().init(this)

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(object : DefaultRefreshHeaderCreater {
            override fun createRefreshHeader(context: Context, layout: RefreshLayout): RefreshHeader {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
                return ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        })

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

        val networkManager = NetworkManager.Builder("http://192.168.2.169:8080/")
                .setBaseServerResponse(BaseResponse::class.java)
                .build(builder.build())
        setTag(Constant.NETWORK_MANAGER_KEY, networkManager)
    }
}
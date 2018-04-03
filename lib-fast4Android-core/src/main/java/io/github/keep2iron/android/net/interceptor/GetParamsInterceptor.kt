package io.github.keep2iron.android.net.interceptor

import java.io.IOException
import java.util.HashMap

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/28 16:02
 *
 *
 * 统一的GET参数添加的拦截器
 */
class GetParamsInterceptor(val listener: (headerParams: Map<String, String>) -> Unit) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()

        val getParams = HashMap<String, String>()
        listener(getParams)
        //添加get参数
        val urlBuilder = original.url().newBuilder()
        for ((key, value) in getParams) {
            urlBuilder.addQueryParameter(key, value)
        }
        val url = urlBuilder.build()
        val requestBuilder = original.newBuilder().method(original.method(), original.body()).url(url)
        original = requestBuilder.build()

        return chain.proceed(original)
    }
}

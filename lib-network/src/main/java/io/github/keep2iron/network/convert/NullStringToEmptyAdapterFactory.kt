package io.github.keep2iron.network.convert

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 16:58
 *
 * 用于将null字符串转换成""
 */
class NullStringToEmptyAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        return if (rawType != String::class.java) {
            null
        } else StringNullAdapter() as TypeAdapter<T>
    }
}
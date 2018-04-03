package io.github.keep2iron.android.net.convert

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.io.IOException

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 16:58
 *
 * 用于将null字符串转换成""
 * @see NullStringToEmptyAdapterFactory
 */
class StringNullAdapter : TypeAdapter<String>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): String {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        return reader.nextString()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: String?) {
        if (value == null) {
            writer.nullValue()
            return
        }
        writer.value(value)
    }
}
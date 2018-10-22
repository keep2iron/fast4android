
package io.github.keep2iron.network.convert;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import io.github.keep2iron.network.IResponseStatus;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @author keep2iron
 * @version 1.0
 * @see retrofit2.converter.gson.GsonConverterFactory
 * @since 2018/03/16 16:58
 */
public class CustomConvertFactory extends Converter.Factory {
	private Gson gson;
	private Class<? extends IResponseStatus> clazz;

	/**
	 * 在这里要传入状态检测的一个测试类
	 *
	 * @param clazz
	 * @return
	 */
	public static CustomConvertFactory create(Class<? extends IResponseStatus> clazz) {
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

		return new CustomConvertFactory(gson, clazz);
	}

	/**
	 * 是否需要包含状态检测
	 *
	 * @param gson  gson实体对象
	 * @param clazz 状态检测的class对象
	 */
	private <C extends IResponseStatus> CustomConvertFactory(@NonNull Gson gson, Class<C> clazz) {
		this.gson = gson;
		this.clazz = clazz;
	}

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		return new CustomResponseConverter<>(gson, type, clazz);
	}

	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

		return new CustomRequestConverter<>(gson, adapter);
	}
}
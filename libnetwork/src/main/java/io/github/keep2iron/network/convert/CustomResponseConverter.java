package io.github.keep2iron.network.convert;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.github.keep2iron.network.IResponseStatus;
import io.github.keep2iron.network.exception.StatusErrorException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by 薛世君
 * Date : 2016/11/9
 * Email : 497881309@qq.com
 */

class CustomResponseConverter<T> implements Converter<ResponseBody, T> {

    public final String CLASS_STRING = "java.lang.String";
    public final String CLASS_RESPONSE_BODY = "okhttp3.ResponseBody";

    private final Gson gson;
    private final Type type;
    private Class<? extends IResponseStatus> clazz;

    <C extends IResponseStatus> CustomResponseConverter(Gson gson, Type type, Class<C> clazz) {
        this.gson = gson;
        this.type = type;
        this.clazz = clazz;
    }

    /**
     * 这里就是错误预处理的函数逻辑
     *
     * @param responseBody 返回的响应实体
     * @return 返回转换的对象
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String body = responseBody.string();
        if (clazz != null) {
            return convertByStatusTest(body);
        }
        return convert(body);
    }

    private T convertByStatusTest(String body) throws StatusErrorException {
        IResponseStatus status = gson.fromJson(body, clazz);

        if (status.isResponseSuccessful()) {
            return convert(body);
        } else {
            throw new StatusErrorException(status);
        }
    }

    private T convert(String body) {
        //如果返回类型是String那么就直接返回String对象,
        if (type instanceof Class) {
            Class clazz = (Class) type;
            if (CLASS_STRING.equals(clazz.getName())) {
                return (T) body;
            }

            //如果直接要求返回responseBody
            if (CLASS_RESPONSE_BODY.equals(clazz.getName())) {
                return (T) body;
            }

            return gson.fromJson(body, type);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return gson.fromJson(body, parameterizedType);
        } else {
            throw new RuntimeException("convert make a error!!");
        }
    }
}

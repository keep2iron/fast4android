package io.github.keep2iron.android.net.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2017/11/28 16:02
 */
public class GetParamsInterceptor implements Interceptor{
    private Map<String, String> mGetParams;

    public GetParamsInterceptor(Map<String, String> getParams) {
        mGetParams = getParams;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        
        Map<String, String> getParams = new HashMap<>();
        getParams.putAll(mGetParams);
        //添加get参数
        HttpUrl.Builder urlBuilder = original.url().newBuilder();
        for (Map.Entry<String, String> entry : getParams.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        HttpUrl url = urlBuilder.build();
        Request.Builder requestBuilder = original.newBuilder().method(original.method(), original.body()).url(url);
        original = requestBuilder.build();

        Response response = chain.proceed(original);

        return response;
    }
}

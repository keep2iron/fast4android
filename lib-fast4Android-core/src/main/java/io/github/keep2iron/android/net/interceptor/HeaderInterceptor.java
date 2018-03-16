/*
 * Create bt Keep2iron on 17-5-25 下午2:33
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.net.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author keep2iron
 */
public abstract class HeaderInterceptor implements Interceptor {

//    private Map<String, String> mHeaderParams;
//
//    public HeaderInterceptor(Map<String, String> headerParams) {
//        mHeaderParams = headerParams;
//    }

    public abstract void putHeader(Map<String, String> headerParams);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        Map<String, String> headerParams = new HashMap<>();
        putHeader(headerParams);
        try {
            Request.Builder builder = request.newBuilder();
            for (Map.Entry<String,String> entry :headerParams.entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
            newRequest = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return chain.proceed(request);
        }

        return chain.proceed(newRequest);
    }
}
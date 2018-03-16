/*
 * Create bt Keep2iron on 17-5-25 下午2:33
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.net.exception;

import io.github.keep2iron.android.net.convert.CustomConvertFactory;

/**
 * Created by 薛世君
 * Date : 2017/1/13
 * Email : 497881309@qq.com
 * <p>
 * 状态异常
 */
public class StatusErrorException extends RuntimeException {

    private CustomConvertFactory.IResponseStatus mResponse;

    /**
     * * 这里message接收的是服务器返回的错误字符串，后面可以通过gson解析进行获取到
     */
    public StatusErrorException(CustomConvertFactory.IResponseStatus mResponse) {
        this.mResponse = mResponse;
    }

    public CustomConvertFactory.IResponseStatus getResponse() {
        return mResponse;
    }
}
/*
 * Create bt Keep2iron on 17-5-25 下午2:33
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.net.exception

import io.github.keep2iron.android.net.convert.CustomConvertFactory

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 18:55
 *
 * 状态异常
 */
class StatusErrorException
/**
 * * 这里message接收的是服务器返回的错误字符串，后面可以通过gson解析进行获取到
 */
(val response: CustomConvertFactory.IResponseStatus) : RuntimeException()
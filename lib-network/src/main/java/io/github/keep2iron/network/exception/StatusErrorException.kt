package io.github.keep2iron.network.exception

import io.github.keep2iron.network.IResponseStatus

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/16 18:55
 *
 * 状态异常
 * 这里message接收的是服务器返回的错误字符串，后面可以通过gson解析进行获取到
 */
class StatusErrorException(val response: IResponseStatus) : RuntimeException()
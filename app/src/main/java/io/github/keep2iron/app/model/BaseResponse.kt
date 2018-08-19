package io.github.keep2iron.app.model

import io.github.keep2iron.network.IResponseStatus

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/16 14:18
 */
class BaseResponse<T> : IResponseStatus {
    var value: T? = null
    var code: Int = 200
    var message: String = "successful"

    override val isResponseSuccessful: Boolean = code == 200
}
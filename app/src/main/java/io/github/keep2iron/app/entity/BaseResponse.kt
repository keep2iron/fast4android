package io.github.keep2iron.app.entity

import io.github.keep2iron.android.net.convert.CustomConvertFactory

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/16 14:18
 */
class BaseResponse<T> : CustomConvertFactory.IResponseStatus {
    override fun isResponseSuccessful(): Boolean = code == 200

    var value: T? = null
    var code: Int = 200
    var message: String = "successful"
}
package io.github.keep2iron.network

interface IResponseStatus {
    /**
     * @return 是否响应成功
     */
    val isResponseSuccessful: Boolean
}
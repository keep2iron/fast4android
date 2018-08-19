package io.github.keep2iron.app.util

import io.github.keep2iron.android.AbstractApplication
import io.github.keep2iron.network.NetworkManager

/**
 * 用于编写扩展方法的方法类
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/21 14:05
 */
fun AbstractApplication.getNetworkManager(): NetworkManager {
    return getTag(Constant.NETWORK_MANAGER_KEY) as NetworkManager
}
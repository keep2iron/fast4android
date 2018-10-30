package io.github.keep2iron.android.load

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:03
 */
interface Refreshable {
    fun setRefreshEnable(isEnable: Boolean)

    fun refresh()

    fun showRefreshComplete()
}
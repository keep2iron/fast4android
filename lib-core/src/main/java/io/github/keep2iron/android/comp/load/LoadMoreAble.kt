package io.github.keep2iron.android.comp.load

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:15
 */
/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/10 11:59
 */
interface LoadMoreAble {

    fun setLoadMoreEnable(isEnable: Boolean)

    fun showLoadMoreComplete()

    fun showLoadMoreFailed()

    fun showLoadMoreEnd()

}
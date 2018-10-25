package io.github.keep2iron.android.comp.load

import io.github.keep2iron.android.comp.adapter.LoadMoreAdapter

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 18:23
 *
 * 针对[LoadMoreAdapter]的一个封装
 */
class VLayoutLoadMoreAble(val adapter: LoadMoreAdapter) : LoadMoreAble {
    override fun setLoadMoreEnable(isEnable: Boolean) {
        adapter.setEnableLoadMore(isEnable)
    }

    override fun showLoadMoreComplete() {
        adapter.showLoadMoreComplete()
    }

    override fun showLoadMoreFailed() {
        adapter.showLoadMoreFailed()
    }

    override fun showLoadMoreEnd() {
        adapter.showLoadMoreEnd()
    }
}
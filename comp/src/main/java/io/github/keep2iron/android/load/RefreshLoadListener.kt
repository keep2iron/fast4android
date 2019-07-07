package io.github.keep2iron.android.load

interface RefreshLoadListener {
    fun onLoad(adapters: RefreshWithLoadMoreAdapter, pager: Pager)

    fun defaultValue(): Any {
        return 0
    }
}
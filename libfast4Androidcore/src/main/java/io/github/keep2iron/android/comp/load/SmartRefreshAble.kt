package io.github.keep2iron.android.comp.load

import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/02 17:02
 *
 * 用于实现了[com.scwang.smartrefresh.layout.SmartRefreshLayout]布局的代理类
 */
class SmartRefreshAble(var layout: SmartRefreshLayout, listener: () -> Unit) : Refreshable {
    init {
        layout.setOnRefreshListener {
            listener()
        }
    }

    override fun setRefreshEnable(isEnable: Boolean) {
        layout.isEnableRefresh = isEnable
    }

    override fun refresh() {
        layout.autoRefresh()
    }

    override fun showRefreshComplete() {
        layout.finishRefresh()
    }
}
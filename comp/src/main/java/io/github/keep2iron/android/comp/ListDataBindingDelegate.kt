package io.github.keep2iron.android.comp

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import io.github.keep2iron.android.databinding.ListBundle
import io.github.keep2iron.android.load.RefreshLoadListener
import java.util.*

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/8
 *
 * 为列表而生, 使用DataBinding绑定数据
 */
interface ListDataBindingDelegate {
    /**
     * 刷新布局
     */
    fun refreshView(): View

    /**
     * 渲染adapter集合
     */
    fun adapters(): ArrayList<DelegateAdapter.Adapter<*>>

    fun refreshLoadListener(): RefreshLoadListener

    /**
     * item map的映射集合
     */
    fun itemTypeMap(): Map<Int, Int> {
        return mapOf()
    }

    fun buildRefreshBundle(): ListBundle {
        return ListBundle(refreshView(),
                adapters(),
                refreshLoadListener(),
                map = itemTypeMap()
        )
    }
}
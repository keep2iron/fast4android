package io.github.keep2iron.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import io.github.keep2iron.android.comp.R

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/01/24 10:48
 *
 * 默认实现的LoadMoreAdapter
 */
class LoadMoreAdapter constructor(context: Context,
                                  recyclerView: RecyclerView
) : AbstractLoadMoreAdapter(context.applicationContext, recyclerView) {

    override fun getLayoutId(): Int {
        return R.layout.item_load_more
    }

}
package io.github.keep2iron.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.alibaba.android.vlayout.VirtualLayoutManager
import java.lang.Exception

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
class WrapperVirtualLayoutManager(context: Context) : VirtualLayoutManager(context) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignore: Exception) {
        }
    }

}
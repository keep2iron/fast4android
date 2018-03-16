package io.github.keep2iron.android.comp.databinding

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 15:25
 */
class RecyclerViewChangeAdapter<T>(private val mAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

    override fun onChanged(sender: ObservableList<T>) {
        mAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
        mAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount)
    }
}

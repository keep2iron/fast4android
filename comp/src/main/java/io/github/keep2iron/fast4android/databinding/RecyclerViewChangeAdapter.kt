package io.github.keep2iron.fast4android.databinding

import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/17 15:25
 */
open class RecyclerViewChangeAdapter<T>(private val mAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<out androidx.recyclerview.widget.RecyclerView.ViewHolder>) :
  ObservableList.OnListChangedCallback<ObservableList<T>>() {

  override fun onChanged(sender: ObservableList<T>) {
    mAdapter.notifyDataSetChanged()
  }

  override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mAdapter.notifyItemRangeChanged(positionStart, itemCount)
  }

  override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mAdapter.notifyItemRangeInserted(positionStart, itemCount)
  }

  override fun onItemRangeMoved(
    sender: ObservableList<T>,
    fromPosition: Int,
    toPosition: Int,
    itemCount: Int
  ) {
    mAdapter.notifyItemMoved(fromPosition, toPosition)
  }

  override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mAdapter.notifyItemRangeRemoved(positionStart, itemCount)
  }
}

package io.github.keep2iron.fast4android.databinding

import androidx.databinding.ObservableList
import androidx.viewpager.widget.PagerAdapter

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/21 13:12
 */
open class ViewPagerChangeAdapter<T>(private val mPagerAdapter: androidx.viewpager.widget.PagerAdapter) :
  ObservableList.OnListChangedCallback<ObservableList<T>>() {

  override fun onChanged(sender: ObservableList<T>) {
    mPagerAdapter.notifyDataSetChanged()
  }

  override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mPagerAdapter.notifyDataSetChanged()
  }

  override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mPagerAdapter.notifyDataSetChanged()
  }

  override fun onItemRangeMoved(
    sender: ObservableList<T>,
    fromPosition: Int,
    toPosition: Int,
    itemCount: Int
  ) {
    mPagerAdapter.notifyDataSetChanged()
  }

  override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
    mPagerAdapter.notifyDataSetChanged()
  }
}

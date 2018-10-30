package io.github.keep2iron.android.databinding

import android.databinding.ObservableList
import android.support.v4.view.PagerAdapter

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/21 13:12
 */
class ViewPagerChangeAdapter<T>(private val mPagerAdapter: PagerAdapter) : ObservableList.OnListChangedCallback<ObservableList<T>>() {

    override fun onChanged(sender: ObservableList<T>) {
        mPagerAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mPagerAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mPagerAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
        mPagerAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
        mPagerAdapter.notifyDataSetChanged()
    }
}

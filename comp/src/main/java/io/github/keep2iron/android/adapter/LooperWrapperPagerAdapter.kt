package io.github.keep2iron.android.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * 用于控制循环轮播图时的adapter
 */
class LooperWrapperPagerAdapter<T : RecyclerViewHolder>(val recyclerAdapter: RecyclerView.Adapter<T>,
                                                        private val pool: RecyclerView.RecycledViewPool) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var realPosition = (position - 1) % recyclerAdapter.itemCount
        if (realPosition < 0)
            realPosition += recyclerAdapter.itemCount

        val itemViewType = recyclerAdapter.getItemViewType(realPosition)
        var holder = pool.getRecycledView(itemViewType)
        if (holder == null) {
            holder = recyclerAdapter.createViewHolder(container, itemViewType);
        }

        recyclerAdapter.onBindViewHolder(holder as T, realPosition)
        container.addView(holder.itemView, ViewPager.LayoutParams())

        return holder.itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int {
        return if (recyclerAdapter.itemCount <= 1) recyclerAdapter.itemCount
        else recyclerAdapter.itemCount + 2
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is RecyclerView.ViewHolder) {
            container.removeView(`object`.itemView)
            pool.putRecycledView(`object`)
        }
    }

    fun getRealCount(): Int {
        return recyclerAdapter.itemCount
    }
}
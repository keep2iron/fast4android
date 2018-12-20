package io.github.keep2iron.android.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/12/19
 */
class WrapperPagerAdapter<T : RecyclerViewHolder>(private val recyclerAdapter: RecyclerView.Adapter<T>,
                                                  private val pool: RecyclerView.RecycledViewPool) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemViewType = recyclerAdapter.getItemViewType(position)
        var holder = pool.getRecycledView(itemViewType)
        if (holder == null) {
            holder = recyclerAdapter.createViewHolder(container, itemViewType)
        }

        recyclerAdapter.onBindViewHolder(holder as T, position)
        container.addView(holder.itemView, ViewPager.LayoutParams())

        return holder.itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = recyclerAdapter.itemCount

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is RecyclerView.ViewHolder) {
            container.removeView(`object`.itemView)
            pool.putRecycledView(`object`)
        }
    }
}
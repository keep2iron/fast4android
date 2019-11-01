package io.github.keep2iron.looplayout

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * 用于控制循环轮播图时的adapter
 */
class LooperWrapperPagerAdapter<T : RecyclerView.ViewHolder>(val recyclerAdapter: RecyclerView.Adapter<T>,
                                                             private val pool: RecyclerView.RecycledViewPool) : PagerAdapter() {

    val spaceItemCount = 2

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var realPosition = (position - spaceItemCount) % recyclerAdapter.itemCount
        if (realPosition < 0)
            realPosition += recyclerAdapter.itemCount

        val itemViewType = recyclerAdapter.getItemViewType(realPosition)
        var holder = pool.getRecycledView(itemViewType)
        if (holder == null) {
            holder = recyclerAdapter.createViewHolder(container, itemViewType)
        }

        recyclerAdapter.onBindViewHolder(holder as T, realPosition)
        container.addView(holder.itemView, ViewPager.LayoutParams())

        return holder.itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int {
//        return if (recyclerAdapter.itemCount <= 1) recyclerAdapter.itemCount
        return recyclerAdapter.itemCount + spaceItemCount * 2
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
package io.github.keep2iron.fast4android.adapter

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup

/**
 * 用于控制循环轮播图时的adapter
 */
class LooperWrapperPagerAdapter<T : ViewHolder>(
  val recyclerAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<T>,
  private val pool: androidx.recyclerview.widget.RecyclerView.RecycledViewPool
) : androidx.viewpager.widget.PagerAdapter() {

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
    container.addView(holder.itemView, androidx.viewpager.widget.ViewPager.LayoutParams())

    return holder.itemView
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

  override fun getCount(): Int {
    return if (recyclerAdapter.itemCount <= 1) recyclerAdapter.itemCount
    else recyclerAdapter.itemCount + 2
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    if (`object` is ViewHolder) {
      container.removeView(`object`.itemView)
      pool.putRecycledView(`object`)
    }
  }

  fun getRealCount(): Int {
    return recyclerAdapter.itemCount
  }
}
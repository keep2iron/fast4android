package io.github.keep2iron.fast4android.adapter

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/12/19
 */
class WrapperPagerAdapter<T : androidx.recyclerview.widget.RecyclerView.ViewHolder>(
  private val recyclerAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<T>,
  private val pool: androidx.recyclerview.widget.RecyclerView.RecycledViewPool
) : androidx.viewpager.widget.PagerAdapter() {

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val itemViewType = recyclerAdapter.getItemViewType(position)
    var holder = pool.getRecycledView(itemViewType)
    if (holder == null) {
      holder = recyclerAdapter.createViewHolder(container, itemViewType)
    }

    recyclerAdapter.onBindViewHolder(holder as T, position)
    container.addView(holder.itemView, androidx.viewpager.widget.ViewPager.LayoutParams())

    return holder.itemView
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

  override fun getCount(): Int = recyclerAdapter.itemCount

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    if (`object` is androidx.recyclerview.widget.RecyclerView.ViewHolder) {
      container.removeView(`object`.itemView)
      pool.putRecycledView(`object`)
    }
  }
}
package io.github.keep2iron.looplayout

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 用于控制循环轮播图时的adapter
 */
class LooperWrapperPagerAdapter<T : RecyclerView.ViewHolder>(val recyclerAdapter: RecyclerView.Adapter<T>)
  : RecyclerView.Adapter<T>() {

  val spaceItemCount = 2

  fun getRealCount(): Int {
    return recyclerAdapter.itemCount
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
    return recyclerAdapter.createViewHolder(parent, viewType)
  }

  override fun getItemCount(): Int {
    return recyclerAdapter.itemCount + spaceItemCount * 2
  }

  override fun onBindViewHolder(holder: T, position: Int) {
    val realPosition = toRealPosition(position)
    val itemViewType = recyclerAdapter.getItemViewType(realPosition)
    recyclerAdapter.onBindViewHolder(holder, realPosition)
  }

  override fun getItemViewType(position: Int): Int {
    return recyclerAdapter.getItemViewType(toRealPosition(position))
  }

  /**
   * 返回真实的位置
   *
   * @param position
   * @return 下标从0开始
   */
  fun toRealPosition(position: Int): Int {
    var realPosition = (position - spaceItemCount) % getRealCount()
    if (realPosition < 0)
      realPosition += getRealCount()
    return realPosition
  }
}
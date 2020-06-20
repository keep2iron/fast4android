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

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    recyclerAdapter.onAttachedToRecyclerView(recyclerView)
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    recyclerAdapter.onDetachedFromRecyclerView(recyclerView)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
    return recyclerAdapter.createViewHolder(parent, viewType)
  }

  override fun getItemCount(): Int {
    return  if(recyclerAdapter.itemCount == 0){
      0
    }else {
      recyclerAdapter.itemCount + spaceItemCount * 2
    }
  }

  override fun onBindViewHolder(holder: T, position: Int) {
    if(getRealCount() == 0) return
    val realPosition = toRealPosition(position)
    recyclerAdapter.onBindViewHolder(holder, realPosition)
  }

  override fun getItemViewType(position: Int): Int {
    return if (getRealCount() == 0) {
      1024
    } else {
      recyclerAdapter.getItemViewType(toRealPosition(position))
    }
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

  init {
    val outerAdapter = this
    recyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onChanged() {
        outerAdapter.notifyDataSetChanged()
      }

      override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        outerAdapter.notifyItemRangeChanged(positionStart + spaceItemCount, itemCount)
        outerAdapter.notifyItemRangeChanged(0, spaceItemCount)
        outerAdapter.notifyItemRangeChanged(recyclerAdapter.itemCount, spaceItemCount)
      }

      override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        // fallback to onItemRangeChanged(positionStart, itemCount) if app
        // does not override this method.
        onItemRangeChanged(positionStart, itemCount)
      }

      override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        outerAdapter.notifyDataSetChanged()
      }

      override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        outerAdapter.notifyDataSetChanged()
      }
    })
  }
}
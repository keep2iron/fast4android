package io.github.keep2iron.fast4android.tabsegment

import android.view.View
import kotlin.properties.Delegates

abstract class TabSegmentAdapter {

  var normalColor by Delegates.notNull<Int>()

  var selectColor by Delegates.notNull<Int>()

  abstract fun createTab(parentView: View, index: Int, selected: Boolean): View

  abstract fun onBindTab(view: View, index: Int, selected: Boolean)

  abstract fun getItemSize(): Int

  internal val mObservable = AdapterDataObservable()

  fun notifyDataSetChanged() {
    mObservable.notifyChanged()
  }

  fun notifyItemChanged(position: Int) {
    mObservable.notifyItemRangeChanged(position, 1)
  }

  fun notifyItemRangeChanged(position: Int, itemCount: Int) {
    mObservable.notifyItemRangeChanged(position, itemCount)
  }

  fun notifyItemInsert(position: Int) {
    mObservable.notifyItemRangeInserted(position, 1)
  }

  fun notifyItemRangeInsert(position: Int, itemCount: Int) {
    mObservable.notifyItemRangeInserted(position, itemCount)
  }

  fun notigyItemRemoved(position: Int) {
    mObservable.notifyItemRangeRemoved(position, 1)
  }

  fun notifyItemRangeRemoved(position: Int, itemCount: Int) {
    mObservable.notifyItemRangeRemoved(position, itemCount)
  }

}
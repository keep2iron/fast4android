package io.github.keep2iron.fast4android.tabsegment

internal abstract class AdapterDataObserver {
  open fun onChanged() {
    // Do nothing
  }

  open fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
    // do nothing
  }

  open fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
    // fallback to onItemRangeChanged(positionStart, itemCount) if app
    // does not override this method.
    onItemRangeChanged(positionStart, itemCount)
  }

  open fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
    // do nothing
  }

  open fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
    // do nothing
  }

  open fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
    // do nothing
  }
}
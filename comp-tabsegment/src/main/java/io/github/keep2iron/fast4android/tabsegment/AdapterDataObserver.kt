package io.github.keep2iron.fast4android.tabsegment

interface AdapterDataObserver {
    fun onChanged() {
        // Do nothing
    }

    fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        // do nothing
    }

    fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        // fallback to onItemRangeChanged(positionStart, itemCount) if app
        // does not override this method.
        onItemRangeChanged(positionStart, itemCount)
    }

    fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        // do nothing
    }

    fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        // do nothing
    }

    fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        // do nothing
    }
}
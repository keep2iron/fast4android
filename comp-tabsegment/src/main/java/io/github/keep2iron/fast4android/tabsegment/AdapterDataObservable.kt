package io.github.keep2iron.fast4android.tabsegment

import android.database.Observable

internal class AdapterDataObservable : Observable<AdapterDataObserver>() {

  fun hasObservers(): Boolean {
    return mObservers.isNotEmpty()
  }

  fun notifyChanged() {
    // since onChanged() is implemented by the app, it could do anything, including
    // removing itself from {@link mObservers} - and that could cause problems if
    // an iterator is used on the ArrayList {@link mObservers}.
    // to avoid such problems, just march thru the list in the reverse order.
    for (i in mObservers.indices.reversed()) {
      mObservers[i].onChanged()
    }
  }

  @JvmOverloads fun notifyItemRangeChanged(
    positionStart: Int, itemCount: Int,
    payload: Any? = null
  ) {
    // since onItemRangeChanged() is implemented by the app, it could do anything, including
    // removing itself from {@link mObservers} - and that could cause problems if
    // an iterator is used on the ArrayList {@link mObservers}.
    // to avoid such problems, just march thru the list in the reverse order.
    for (i in mObservers.indices.reversed()) {
      mObservers[i].onItemRangeChanged(positionStart, itemCount, payload)
    }
  }

  fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
    // since onItemRangeInserted() is implemented by the app, it could do anything,
    // including removing itself from {@link mObservers} - and that could cause problems if
    // an iterator is used on the ArrayList {@link mObservers}.
    // to avoid such problems, just march thru the list in the reverse order.
    for (i in mObservers.indices.reversed()) {
      mObservers[i].onItemRangeInserted(positionStart, itemCount)
    }
  }

  fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
    // since onItemRangeRemoved() is implemented by the app, it could do anything, including
    // removing itself from {@link mObservers} - and that could cause problems if
    // an iterator is used on the ArrayList {@link mObservers}.
    // to avoid such problems, just march thru the list in the reverse order.
    for (i in mObservers.indices.reversed()) {
      mObservers[i].onItemRangeRemoved(positionStart, itemCount)
    }
  }

  fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
    for (i in mObservers.indices.reversed()) {
      mObservers[i].onItemRangeMoved(fromPosition, toPosition, 1)
    }
  }
}
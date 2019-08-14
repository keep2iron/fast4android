package io.github.keep2iron.fast4android.databinding

import androidx.databinding.ObservableList
import android.util.Log
import java.lang.ref.WeakReference

open class WeakOnListChangedCallback<T : ObservableList<*>>(delegate: ObservableList.OnListChangedCallback<T>) :
  ObservableList.OnListChangedCallback<T>() {

  private val ref: WeakReference<ObservableList.OnListChangedCallback<T>> = WeakReference(delegate)

  override fun onChanged(sender: T) {
    getListener()?.onChanged(sender)
  }

  override fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int) {
    getListener()?.onItemRangeChanged(sender, positionStart, itemCount)
  }

  override fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int) {
    getListener()?.onItemRangeInserted(sender, positionStart, itemCount)
  }

  override fun onItemRangeMoved(sender: T, fromPosition: Int, toPosition: Int, itemCount: Int) {
    getListener()?.onItemRangeMoved(sender, fromPosition, toPosition, itemCount)
  }

  override fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int) {
    getListener()?.onItemRangeRemoved(sender, positionStart, itemCount)
  }

  private fun getListener(): ObservableList.OnListChangedCallback<T>? {
    val listener = ref.get()
    if (listener == null) {
      Log.d(WeakOnListChangedCallback::class.java.simpleName, "ref.get() is null!")
    }
    return listener
  }
}
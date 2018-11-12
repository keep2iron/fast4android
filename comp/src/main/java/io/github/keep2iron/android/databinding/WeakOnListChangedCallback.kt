package io.github.keep2iron.android.databinding

import android.databinding.ObservableList

import java.lang.ref.WeakReference

open class WeakOnListChangedCallback<T : ObservableList<*>>(delegate: ObservableList.OnListChangedCallback<T>) : ObservableList.OnListChangedCallback<T>() {

    private val ref: WeakReference<ObservableList.OnListChangedCallback<T>> = WeakReference(delegate)

    override fun onChanged(sender: T) {
        ref.get()!!.onChanged(sender)
    }

    override fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int) {
        ref.get()!!.onItemRangeChanged(sender, positionStart, itemCount)
    }

    override fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int) {
        ref.get()!!.onItemRangeInserted(sender, positionStart, itemCount)
    }

    override fun onItemRangeMoved(sender: T, fromPosition: Int, toPosition: Int, itemCount: Int) {
        ref.get()!!.onItemRangeMoved(sender, fromPosition, toPosition, itemCount)
    }

    override fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int) {
        ref.get()!!.onItemRangeRemoved(sender, positionStart, itemCount)
    }
}
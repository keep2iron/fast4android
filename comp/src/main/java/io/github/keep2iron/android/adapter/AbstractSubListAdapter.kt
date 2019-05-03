package io.github.keep2iron.android.adapter

import android.databinding.ObservableList
import android.support.v7.util.DiffUtil
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
abstract class AbstractSubListAdapter<T>(viewType: Int,
                                         list: ObservableList<T>,
                                         diffCallback: DiffUtil.ItemCallback<T>?) : AbstractSubAdapter(viewType) {
    init {
        list.addOnListChangedCallback(RecyclerViewChangeAdapter<T>(this))
    }

}
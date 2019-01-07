package io.github.keep2iron.android.adapter

import android.content.Context
import android.databinding.ObservableList
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.util.DiffUtil
import io.github.keep2iron.android.collections.AsyncDiffObservableList

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
abstract class AbstractSubDiffAdapter<T>(context: Context,
                                         viewType: Int = 0,
                                         list: AsyncDiffObservableList<T>,
                                         diffCallback: DiffUtil.ItemCallback<T>?) : AbstractSubAdapter(viewType) {
    private var asyncListDiffer: AsyncListDiffer<T>? = null

    init {
        diffCallback?.apply {
            asyncListDiffer = AsyncListDiffer(AdapterListUpdateCallback(this@AbstractSubDiffAdapter), AsyncDifferConfig.Builder<T>(diffCallback).build())
        }
    }

}
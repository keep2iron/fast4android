package io.github.keep2iron.app.module.mutile

import android.content.Context
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.MultiTypeAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/9
 */
class Type2Adapter(context: Context) : MultiTypeAdapter.SubMultiTypeAdapter(context, 2) {
    override fun getLayoutId(): Int = R.layout.item_type2

    override fun render(holder: RecyclerViewHolder, position: Int) {
    }
}
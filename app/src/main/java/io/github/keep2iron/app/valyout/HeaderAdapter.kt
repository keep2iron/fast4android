package io.github.keep2iron.app.valyout

import android.content.Context
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/1
 */
class HeaderAdapter(context: Context) : AbstractSubAdapter( 0) {
    override fun getLayoutId(): Int = R.layout.item_header

    override fun render(holder: RecyclerViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 1

    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()
}
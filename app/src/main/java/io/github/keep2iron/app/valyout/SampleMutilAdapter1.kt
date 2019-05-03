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
 * @date 2018/10/30
 */
class SampleMutilAdapter1(context: Context) : AbstractSubAdapter( 0) {
    override fun getLayoutId(): Int = R.layout.comp_item_load_more

    override fun render(holder: RecyclerViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = ILLEGAL_SIZE

    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()
}
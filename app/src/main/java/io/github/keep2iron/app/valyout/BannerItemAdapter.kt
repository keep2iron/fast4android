package io.github.keep2iron.app.valyout

import android.content.Context
import android.widget.ImageView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.comp.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.app.BR
import io.github.keep2iron.app.R
import io.github.keep2iron.app.ui.RecommendModel
import io.github.keep2iron.app.util.Constant

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:22
 */
class BannerItemAdapter(context: Context, private val viewModule: RecommendModel) : AbstractSubAdapter(context) {
    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

    override fun render(holder: RecyclerViewHolder, position: Int) {
        holder.getViewDataBinding()?.setVariable(BR.indexModel, viewModule.bannerItems[position])
    }

    override fun getItemCount(): Int = viewModule.bannerItems.size

    override fun getLayoutId(): Int = R.layout.banner_recycle_subitem

    override fun getItemViewType(position: Int): Int {
        return Constant.RECYCLE_BANNER_SUB_ITEM
    }
}
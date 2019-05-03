package io.github.keep2iron.app.valyout

import android.content.Context
import android.support.v4.app.FragmentManager
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.app.BR
import io.github.keep2iron.app.R
import io.github.keep2iron.app.ui.ColorFragment
import io.github.keep2iron.app.module.recommend.RecommendModel
import io.github.keep2iron.app.util.Constant

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/12 11:22
 */
class BannerItemAdapter(private val viewModule: RecommendModel,
                        private val fragmentManager: FragmentManager) : AbstractSubAdapter( Constant.RECYCLE_BANNER_SUB_ITEM) {
    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

    override fun render(holder: RecyclerViewHolder, position: Int) {
        holder.getViewDataBinding()?.setVariable(BR.indexModel, viewModule.bannerItems[position])

        holder.itemView.setOnClickListener {
            val beginTransaction = fragmentManager.beginTransaction()
            beginTransaction.replace(R.id.container, ColorFragment.getInstance(R.color.colorAccent))
            beginTransaction.addToBackStack(null)
            beginTransaction.commit()
        }
    }

    override fun getItemCount(): Int = viewModule.bannerItems.size

    override fun getLayoutId(): Int = R.layout.banner_recycle_subitem
}
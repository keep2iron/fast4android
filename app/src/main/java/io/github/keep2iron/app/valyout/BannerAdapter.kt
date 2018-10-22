package io.github.keep2iron.app.valyout

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper

import io.github.keep2iron.app.R
import io.github.keep2iron.app.util.Constant
import io.github.keep2iron.android.comp.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.android.comp.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.comp.widget.LoopViewLayout
import io.github.keep2iron.android.core.extra.dp2px
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.ui.RecommendModel

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/08 11:42
 */
class BannerAdapter(context: Context,
                    private val indexModule: RecommendModel,
                    private val recyclerView: RecyclerView,
                    private val fragmentManager: FragmentManager) : AbstractSubAdapter(context) {
    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()

    override fun getLayoutId(): Int {
        return R.layout.banner_recycle_item
    }

    override fun getItemViewType(position: Int): Int {
        return Constant.RECYCLE_BANNER_ITEM
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {

        val loopViewLayout = holder.findViewById<LoopViewLayout>(R.id.lvlLoopView)
//        loopViewLayout.setOnEmptyLayoutResId(R.layout.banner_on_empty_layout)
        loopViewLayout.setAdapter(BannerItemAdapter(context, indexModule, fragmentManager), recyclerView.recycledViewPool)
        loopViewLayout.viewPager.clipToPadding = false
        val dp60 = holder.itemView.dp2px(60)
        val dp20 = holder.itemView.dp2px(20)
        loopViewLayout.viewPager.pageMargin = dp20
        loopViewLayout.viewPager.offscreenPageLimit = 3
        loopViewLayout.viewPager.setPadding(dp60, dp20, dp60, dp20)

        indexModule.bannerItems.addOnListChangedCallback(RecyclerViewChangeAdapter<GsonIndex>(loopViewLayout.getRecyclerViewAdapter()))
    }

    override fun getItemCount(): Int {
        return 1
    }
}

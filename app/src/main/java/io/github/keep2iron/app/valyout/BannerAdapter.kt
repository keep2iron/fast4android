package io.github.keep2iron.app.valyout

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import io.github.keep2iron.app.R
import io.github.keep2iron.app.util.Constant
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.widget.LoopViewLayout
import io.github.keep2iron.android.utilities.DisplayUtil
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.module.recommend.RecommendModel

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/08 11:42
 */
class BannerAdapter(context: Context,
                    private val indexModule: RecommendModel,
                    private val recyclerView: RecyclerView,
                    private val fragmentManager: FragmentManager) : AbstractSubAdapter() {
    override fun onCreateLayoutHelper(): LayoutHelper = LinearLayoutHelper()


    override fun getLayoutId(): Int {
        return R.layout.banner_recycle_item
    }

    override fun getItemViewType(position: Int): Int {
        return Constant.RECYCLE_BANNER_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        val loopViewLayout = holder.findViewById<LoopViewLayout>(R.id.lvlLoopView)
        loopViewLayout.setAdapter(BannerItemAdapter(context, indexModule, fragmentManager), recyclerView.recycledViewPool)
        indexModule.bannerItems.addOnListChangedCallback(RecyclerViewChangeAdapter<GsonIndex>(loopViewLayout.getRecyclerViewAdapter()))
        return holder
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
//        val loopViewLayout = holder.findViewById<LoopViewLayout>(R.id.lvlLoopView)
////        loopViewLayout.setOnEmptyLayoutResId(R.layout.banner_on_empty_layout)
////        loopViewLayout.viewPager.clipToPadding = false
////        val dp60 = DisplayUtil.dp2px(context.applicationContext, 60)
////        val dp20 = DisplayUtil.dp2px(context.applicationContext, 20)
////        loopViewLayout.viewPager.pageMargin = dp20
////        loopViewLayout.viewPager.offscreenPageLimit = 3
////        loopViewLayout.viewPager.setPadding(dp60, dp20, dp60, dp20)
    }

    override fun getItemCount(): Int {
        return 1
    }
}

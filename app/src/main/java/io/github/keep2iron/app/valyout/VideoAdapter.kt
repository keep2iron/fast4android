package io.github.keep2iron.app.valyout

import android.content.Context
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import io.github.keep2iron.android.comp.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.android.comp.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.util.CommonUtil
import io.github.keep2iron.app.BR
import io.github.keep2iron.app.R
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.ui.RecommendModel
import io.github.keep2iron.app.util.Constant

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/14 15:42
 */
class VideoAdapter(context: Context, private val indexModule: RecommendModel) : AbstractSubAdapter(context) {
    override fun onCreateLayoutHelper(): LayoutHelper {
        val totalSpan = 2

        val gridLayoutHelper = GridLayoutHelper(totalSpan)
        val util = CommonUtil.getInstance(context)
        val dx = util.getPercentageSize(R.dimen.x30)
        gridLayoutHelper.setAutoExpand(false)
        gridLayoutHelper.hGap = dx.toInt()
        gridLayoutHelper.vGap = dx.toInt()
        val x30 = util.getPercentageSize(R.dimen.x30)
        val y25 = util.getPercentageSize(R.dimen.y25)
        gridLayoutHelper.setMargin(x30.toInt(), y25.toInt(), x30.toInt(), y25.toInt())

        gridLayoutHelper.setSpanSizeLookup(object : GridLayoutHelper.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val realPosition = position - startPosition
                return if (realPosition < indexModule.indexItems.size) {
                    when (indexModule.indexItems[realPosition].type) {
                        GsonIndex.TYPE_ARTICLE -> 1
                        GsonIndex.TYPE_BANNER -> totalSpan
                        GsonIndex.TYPE_VIDEO -> 1
                        else -> 1
                    }
                } else totalSpan
            }
        })

        return gridLayoutHelper
    }

    init {
        indexModule.indexItems.addOnListChangedCallback(RecyclerViewChangeAdapter<String>(this))
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
        holder.getViewDataBinding()?.setVariable(BR.indexModel, indexModule.indexItems[position])
    }

    override fun getItemCount(): Int {
        return indexModule.indexItems.size
    }

    override fun getLayoutId(): Int = R.layout.recycle_item_video

    override fun getItemViewType(position: Int): Int {
        return Constant.RECYCLE_VIDEO_ITEM
    }
}
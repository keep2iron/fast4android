package io.github.keep2iron.app.valyout

import android.content.Context
import android.widget.TextView
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import io.github.keep2iron.android.comp.adapter.AbstractSubAdapter
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import io.github.keep2iron.android.comp.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.util.CommonUtil
import io.github.keep2iron.app.R
import io.github.keep2iron.app.ui.IndexModule
import io.github.keep2iron.app.util.Constant

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/14 15:42
 */
class VideoAdapter(context: Context, private val indexModule: IndexModule) : AbstractSubAdapter(context) {
    override fun onCreateLayoutHelper(): LayoutHelper{
        val gridLayoutHelper = GridLayoutHelper(2)
        val util = CommonUtil.getInstance(context)
        val dx = util.getPercentageSize(R.dimen.x10)
        gridLayoutHelper.setAutoExpand(false)
        gridLayoutHelper.hGap = dx.toInt()
        gridLayoutHelper.vGap = dx.toInt()
        val x30 = util.getPercentageSize(R.dimen.x30)
        val y25 = util.getPercentageSize(R.dimen.y25)
        gridLayoutHelper.setMargin(x30.toInt(), y25.toInt(), x30.toInt(), y25.toInt())

        return gridLayoutHelper
    }

    init {
        indexModule.videoItems.addOnListChangedCallback(RecyclerViewChangeAdapter<String>(this))
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
        holder.findViewById<TextView>(R.id.tvVideoTitle).text = "$position"
    }

    override fun getItemCount(): Int {
        return indexModule.videoItems.size
    }

    override fun getLayoutId(): Int = R.layout.recycle_item_video

    override fun getItemViewType(position: Int): Int {
        return Constant.RECYCLE_VIDEO_ITEM
    }
}
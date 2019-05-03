package io.github.keep2iron.app.valyout

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.Fast4Android
import io.github.keep2iron.android.adapter.AbstractSubAdapter
import io.github.keep2iron.android.adapter.RecyclerViewHolder
import io.github.keep2iron.android.databinding.RecyclerViewChangeAdapter
import io.github.keep2iron.android.ext.dp2px
import io.github.keep2iron.app.BR
import io.github.keep2iron.app.R
import io.github.keep2iron.app.model.GsonIndex
import io.github.keep2iron.app.module.recommend.RecommendFragment
import io.github.keep2iron.app.module.recommend.RecommendModel
import io.github.keep2iron.app.util.Constant
import io.github.keep2iron.pineapple.ImageLoaderManager
import io.github.keep2iron.pineapple.MiddlewareView

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/14 15:42
 */
class VideoAdapter(context: Context,
                   private val indexModule: RecommendModel,
                   private val fragmentManager: FragmentManager) : AbstractSubAdapter(Constant.RECYCLE_VIDEO_ITEM) {

    init {
        indexModule.indexItems.addOnListChangedCallback(RecyclerViewChangeAdapter<GsonIndex>(this))
        setOnItemClickListener {
            Logger.d("position :${it}")
        }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        val totalSpan = 2

        val gridLayoutHelper = GridLayoutHelper(2)
        gridLayoutHelper.hGap = dp2px(8)
        gridLayoutHelper.vGap = dp2px(8)
        gridLayoutHelper.bgColor = ContextCompat.getColor(Fast4Android.CONTEXT, android.R.color.darker_gray)
        gridLayoutHelper.paddingLeft = dp2px(12)
        gridLayoutHelper.paddingRight = dp2px(12)
        gridLayoutHelper.setAutoExpand(true)

        return gridLayoutHelper
    }

    init {
        indexModule.indexItems.addOnListChangedCallback(RecyclerViewChangeAdapter<String>(this))
    }

    override fun render(holder: RecyclerViewHolder, position: Int) {
        val movie = indexModule.indexItems[position]
        holder.setText(R.id.tvVideoTitle, movie.movieName ?: "")
        ImageLoaderManager.INSTANCE.showImageView(holder.findViewById<MiddlewareView>(R.id.ivMovieImage), movie.movieImage
                ?: "")

//        holder.itemView.setOnClickListener {
//            val beginTransaction = fragmentManager.beginTransaction()
//            beginTransaction.replace(R.id.container, RecommendFragment.getInstance())
//            beginTransaction.addToBackStack(null)
//            beginTransaction.commit()
//        }
    }

    override fun getItemCount(): Int {
        return indexModule.indexItems.size
    }

    override fun getLayoutId(): Int = R.layout.recycle_item_video
}
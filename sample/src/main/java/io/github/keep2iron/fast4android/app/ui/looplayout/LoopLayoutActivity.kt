package io.github.keep2iron.fast4android.app.ui.looplayout

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.LooplayoutActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.base.util.setStatusBarDark
import io.github.keep2iron.fast4android.base.util.translucent
import io.github.keep2iron.pineapple.ImageLoaderManager
import io.github.keep2iron.pineapple.MiddlewareView
import io.github.keep2iron.pomelo.pager.adapter.AbstractSubListAdapter
import io.github.keep2iron.pomelo.pager.adapter.RecyclerViewHolder

@ParallaxBack
class LoopLayoutActivity : AbstractActivity<LooplayoutActivityBinding>() {

  override fun resId(): Int = R.layout.looplayout_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    translucent()
    setStatusBarDark()

    dataBinding.topbarLayout.setup {
      addLeftBackImageButton().setOnClickListener {
        finish()
      }
    }

    val data = ObservableArrayList<Int>()
    data.addAll(listOf(
      R.mipmap.ic_launcher,
      R.mipmap.example_image1,
      R.mipmap.example_image2,
      R.mipmap.example_image3))

    dataBinding.looperLayout.viewPager.setPadding(0, 0, 0, 0)
    dataBinding.looperLayout.viewPager.pageMargin = 0
    dataBinding.looperLayout.setAdapter(object : AbstractSubListAdapter<Int>(data, 0) {
      override fun onInflateLayoutId(parent: ViewGroup, viewType: Int): Int = R.layout.looplayout_loop_item

      override fun render(holder: RecyclerViewHolder, item: Int, position: Int) {
        val imageView = holder.findViewById<MiddlewareView>(R.id.imageView)
        ImageLoaderManager.getInstance().showImageView(imageView, item)
      }
    })
  }
}
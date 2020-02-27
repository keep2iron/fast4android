package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.os.Bundle
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.TabSegmentListActivityBinding
import io.github.keep2iron.fast4android.app.ui.bottomnav.BottomNavLayoutActivity
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.base.util.push
import io.github.keep2iron.fast4android.base.util.translucent

@ParallaxBack
class TabSegmentListActivity : AbstractActivity<TabSegmentListActivityBinding>() {
  override fun resId(): Int = R.layout.tab_segment_list_activity
  override fun initVariables(savedInstanceState: Bundle?) {
    translucent()

    dataBinding.btnFixed.setOnClickListener {
      push<TabSegmentFixedActivity>()
    }
    dataBinding.btnScrollable.setOnClickListener {
      push<TabSegmentActivity>()
    }
    dataBinding.btnBottomTab.setOnClickListener {
      push<BottomNavLayoutActivity>()
    }
  }
}
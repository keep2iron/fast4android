package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.os.Bundle
import io.github.keep2iron.base.util.translucent
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.databinding.TabSegmentListActivityBinding
import io.github.keep2iron.fast4android.app.ui.bottomnav.BottomNavLayoutActivity
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.base.util.startActivity

@ParallaxBack
class TabSegmentListActivity : AbstractActivity<TabSegmentListActivityBinding>() {
    override fun resId(): Int = R.layout.tab_segment_list_activity
    override fun initVariables(savedInstanceState: Bundle?) {
        translucent()

        dataBinding.btnFixed.setOnClickListener {
            startActivity(TabSegmentFixedActivity::class)
        }
        dataBinding.btnScrollable.setOnClickListener {
            startActivity(TabSegmentActivity::class)
        }
        dataBinding.btnBottomTab.setOnClickListener {
            startActivity(BottomNavLayoutActivity::class)
        }
    }
}
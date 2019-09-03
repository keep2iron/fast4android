package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.R.id
import io.github.keep2iron.fast4android.app.databinding.TabSegmentActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.FindViewById
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.core.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.tabsegment.FastTabSegmentLayout
import io.github.keep2iron.fast4android.tabsegment.TextFastTabSegmentAdapter

@ParallaxBack
class TabSegmentActivity : AbstractActivity<TabSegmentActivityBinding>() {

  private val tabLayout: FastTabSegmentLayout by FindViewById(id.tabLayout)

  private val viewPager: ViewPager by FindViewById(id.viewPager)

  override fun resId(): Int = R.layout.tab_segment_activity

  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)
    dataBinding.topBarLayout.setup {
      addLeftBackImageButton().setOnClickListener {
        finish()
      }
    }
    val tabs = listOf(
      "tab1",
      "tab2",
      "tab3",
      "tab4",
      "tab5",
      "tab6",
      "tab7",
      "tab8",
      "tab9"
    )
    viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
      override fun getItem(position: Int): Fragment {
        return TabSegmentFragment.newInstance(
          arrayOf(
            Color.BLACK,
            Color.BLUE,
            Color.RED
          )[(Math.random() * 3).toInt()]
        )
      }

      override fun getCount(): Int = tabs.size
    }
    tabLayout.tabMode = FastTabSegmentLayout.MODE_SCROLLABLE
    tabLayout.setAdapter(TextFastTabSegmentAdapter(tabs))
    tabLayout.setupWithViewPager(viewPager)
  }
}
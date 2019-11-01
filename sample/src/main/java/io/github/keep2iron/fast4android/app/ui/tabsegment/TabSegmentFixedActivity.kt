package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import io.github.keep2iron.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.R.id
import io.github.keep2iron.fast4android.app.databinding.TabSegmentActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.fast4android.tabsegment.FastTabSegmentLayout
import io.github.keep2iron.fast4android.tabsegment.TabSegmentAdapter

@ParallaxBack
class TabSegmentFixedActivity : AbstractActivity<TabSegmentActivityBinding>() {
    private val tabLayout: FastTabSegmentLayout by findViewByDelegate(id.tabLayout)
    private val viewPager: ViewPager by findViewByDelegate(id.viewPager)
    override fun resId(): Int = R.layout.tab_segment_activity
    override fun initVariables(savedInstanceState: Bundle?) {
        FastStatusBarHelper.translucent(this)
        dataBinding.topBarLayout.setup {
            addLeftBackImageButton().setOnClickListener {
                finish()
            }
        }
        val icons = arrayOf(
                R.mipmap.icon_tabbar_component,
                R.mipmap.icon_tabbar_util,
                R.mipmap.icon_tabbar_lab
        )
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return TabSegmentFragment.newInstance(
                        arrayOf(
                                Color.BLACK,
                                Color.BLUE,
                                Color.RED
                        )[(position % 3)]
                )
            }

            override fun getCount(): Int = icons.size
            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        }
        tabLayout.tabMode = FastTabSegmentLayout.MODE_FIXED
        val tabSegmentAdapter = object : TabSegmentAdapter() {
            val selectedIcons = arrayOf(
                    R.mipmap.icon_tabbar_component_selected,
                    R.mipmap.icon_tabbar_util_selected,
                    R.mipmap.icon_tabbar_lab_selected
            )

            override fun createTab(parentView: ViewGroup, index: Int, selected: Boolean): View {
                return ImageView(parentView.context)
            }

            override fun onBindTab(view: View, index: Int, selected: Boolean) {
                (view as ImageView).apply {
                    scaleType = CENTER_INSIDE
                    setImageDrawable(ContextCompat.getDrawable(
                            context, if (selected) {
                        selectedIcons[index]
                    } else {
                        icons[index]
                    }
                    ).also {
                        it?.setBounds(0, 0, dp2px(20), dp2px(20))
                    })
                }
            }

            override fun getItemSize(): Int = icons.size
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setAdapter(tabSegmentAdapter)
    }
}
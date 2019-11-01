package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.R.id
import io.github.keep2iron.fast4android.app.databinding.TabSegmentActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.tabsegment.FastTabSegmentLayout
import io.github.keep2iron.fast4android.tabsegment.TabSegmentAdapter

@ParallaxBack
class TabSegmentActivity : AbstractActivity<TabSegmentActivityBinding>() {

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
        val tabs = mutableListOf<String>()
        val list = listOf(
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
//    tabs.addAll(list)
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

            override fun getCount(): Int = tabs.size

            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        }
        tabLayout.tabMode = FastTabSegmentLayout.MODE_SCROLLABLE
        val tabSegmentAdapter = CustomTabSegmentAdapter(tabs)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setAdapter(tabSegmentAdapter)

        Handler().postDelayed({
            tabs.addAll(list)
            tabSegmentAdapter.notifyDataSetChanged()
        }, 1000)

        Handler().postDelayed({
            tabs.removeAt(tabs.size - 1)
            tabs.removeAt(tabs.size - 1)
            tabs.removeAt(tabs.size - 1)
            tabs.removeAt(tabs.size - 1)
            tabs.removeAt(tabs.size - 1)
            tabs.removeAt(tabs.size - 1)

            tabs.add("测试1")
            tabs.add("测试2")
//      tabs.clear()
            tabSegmentAdapter.notifyDataSetChanged()
        }, 3000)
    }

    class CustomTabSegmentAdapter(private val tabs: List<String>) : TabSegmentAdapter() {

        override fun createTab(parentView: ViewGroup, index: Int, selected: Boolean): View {
            return LayoutInflater.from(parentView.context).inflate(R.layout.item_segment_tab, parentView,false)
        }

        override fun onBindTab(view: View, index: Int, selected: Boolean) {
        }

        override fun getItemSize(): Int = tabs.size
    }
}
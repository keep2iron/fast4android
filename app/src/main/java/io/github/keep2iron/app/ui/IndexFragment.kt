package io.github.keep2iron.app.ui

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.orhanobut.logger.Logger
import io.github.keep2iron.app.R
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.databinding.IndexFragmentBinding

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/08 10:35
 */
class IndexFragment : AbstractFragment<IndexFragmentBinding>() {
    override val resId: Int = R.layout.index_fragment
    private var fragments: ArrayList<Title> = ArrayList()

    override fun initVariables(container: View?) {
        fragments.add(SeriesFragment.getInstance())
        fragments.add(RecommendFragment.getInstance())
        fragments.add(MyWatchingFragment.getInstance())

        dataBinding.viewPager.adapter = IndexFragmentAdapter()
        dataBinding.tabLayout.setupWithViewPager(dataBinding.viewPager)
        dataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val gradientColor = intArrayOf(R.color.blue, R.color.purple, R.color.light_green)
                val gradientStateColor = intArrayOf(R.color.deep_blue, R.color.deep_purple, R.color.deep_light_green)

                dataBinding.gbvGradientView.animatorNextColor(tab.position * 0.33f + 0.33f, gradientColor[tab.position])
                setStatusColor(gradientStateColor[tab.position])
            }
        })
        dataBinding.viewPager.currentItem = 1
    }

    companion object {
        fun getInstance(): IndexFragment {
            return IndexFragment()
        }
    }

    inner class IndexFragmentAdapter : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = fragments[position] as Fragment

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = fragments[position].getTitle()
    }
}
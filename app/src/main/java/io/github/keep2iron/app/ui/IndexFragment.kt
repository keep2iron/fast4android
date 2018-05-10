package io.github.keep2iron.app.ui

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import io.github.keep2iron.app.R
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.databinding.IndexFragmentBinding
import io.github.keep2iron.app.widget.MaterialMenuDrawable

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
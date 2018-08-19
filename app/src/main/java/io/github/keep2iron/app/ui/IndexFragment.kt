package io.github.keep2iron.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
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

    override fun initVariables(container: View?, savedInstanceState: Bundle?) {
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
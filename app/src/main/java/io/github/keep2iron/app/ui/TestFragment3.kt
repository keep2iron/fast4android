package io.github.keep2iron.app.ui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bana.bananasays.libaspect.FragmentVisibleDelegate
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.core.AbstractFragment
import io.github.keep2iron.app.R
import io.github.keep2iron.app.databinding.FragmentTestBinding

class TestFragment3 : AbstractFragment<FragmentTestBinding>() {
    var delegate: FragmentVisibleDelegate = FragmentVisibleDelegate(this) {
        Logger.d("${this.javaClass.simpleName} show $it")
    }

    override val resId: Int = R.layout.fragment_test

    override fun initVariables(container: View, savedInstanceState: Bundle?) {
        dataBinding.textView.text = this.javaClass.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Logger.d("onCreateView ${this.javaClass.simpleName}")
        delegate.onCreateView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
//        Logger.d("onResume ${this.javaClass.simpleName}")
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        Logger.d("${this.javaClass.simpleName} setUserVisibleHint $isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)
        delegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
//        Logger.d("${this.javaClass.simpleName} onHiddenChanged $hidden")
        super.onHiddenChanged(hidden)
        delegate.onHiddenChanged(hidden)
    }
}

package io.github.keep2iron.android.core

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import io.github.keep2iron.android.comp.swipe.SwipeBackLayout
import io.github.keep2iron.android.comp.swipe.Utils
import io.github.keep2iron.android.comp.swipe.app.SwipeBackActivityBase
import io.github.keep2iron.android.comp.swipe.app.SwipeBackActivityHelper

/**
 * @author keep2iron
 */
abstract class AbstractSwipeBackActivity<DB : ViewDataBinding> : AbstractActivity<DB>(), SwipeBackActivityBase {
    private lateinit var mHelper: SwipeBackActivityHelper

    override fun beforeInit() {
        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun <T : View> findViewById(id: Int): T {
        val v = super.findViewById<T>(id)
        return v ?: mHelper.findViewById(id) as T
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }
}

package com.bana.bananasays.libaspect

import android.support.v4.app.Fragment
import com.orhanobut.logger.Logger


/**
 * 文件描述：.
 * 作者：Created by banana on 2018/10/31.
 * 版本号：1.0
 *
 * 用于控制fragment显示隐藏的代理类对象
 */
class FragmentVisibleDelegate(private val fragment: Fragment,
                              private val listener: (Boolean) -> Unit) : FragmentVisible {
    internal enum class VisibilityMode {
        DEFAULT,

        ON_HIDE_CHANGED,

        SET_USER_VISIBLE_HINT,
    }

    private var isVisibleToUsers: Boolean = false

    private var executeOnCreateView: Boolean = false

    private var mode = VisibilityMode.DEFAULT

    private fun setVisibleToUser(isVisibleToUser: Boolean) {
        if (!fragment.isAdded) {
            return
        }
        if (isVisibleToUser == isVisibleToUsers) {
            return
        }
        isVisibleToUsers = isVisibleToUser
        listener(isVisibleToUsers)
    }

    override fun onCreateView() {
//        executeOnCreateView = true
    }


    override fun onResume() {
        //只改变当前可见的fragment
        val isParentVisible = (fragment.parentFragment != null && !fragment.parentFragment!!.isHidden && fragment.parentFragment!!.userVisibleHint) || fragment.parentFragment == null

        if (isParentVisible && fragment.userVisibleHint && mode == VisibilityMode.SET_USER_VISIBLE_HINT) {
            setVisibleToUser(true)
        }

        if (isParentVisible && (!fragment.isHidden) && mode != VisibilityMode.SET_USER_VISIBLE_HINT) {
            setVisibleToUser(true)
        }
    }

    override fun onPause() {
        val isParentVisible = (fragment.parentFragment != null && !fragment.parentFragment!!.isHidden && fragment.parentFragment!!.userVisibleHint) || fragment.parentFragment == null

        if (isParentVisible && fragment.userVisibleHint && mode == VisibilityMode.SET_USER_VISIBLE_HINT) {
            setVisibleToUser(false)
        }

        if (isParentVisible && (!fragment.isHidden) && mode != VisibilityMode.SET_USER_VISIBLE_HINT) {
            setVisibleToUser(false)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        mode = VisibilityMode.SET_USER_VISIBLE_HINT
        setVisibleToUser(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        mode = VisibilityMode.ON_HIDE_CHANGED
        setVisibleToUser(!hidden)
    }
}
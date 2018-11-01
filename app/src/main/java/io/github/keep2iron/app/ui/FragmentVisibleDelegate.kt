package com.bana.bananasays.libaspect


/**
 * 文件描述：.
 * 作者：Created by banana on 2018/10/31.
 * 版本号：1.0
 *
 * 用于控制fragment显示隐藏的代理类对象
 */
class FragmentVisibleDelegate(private val listener: (Boolean) -> Unit) : FragmentVisible {
    internal enum class VisibilityState {
        DEFAULT,

        ON_CREATE,

        VISIBLE,

        HIDE,
    }

    private var isVisibleToUsers: Boolean = false

    private var onHideChangedState: VisibilityState = VisibilityState.DEFAULT

    private var onSetUserVisibleHintState: VisibilityState = VisibilityState.DEFAULT

    private fun setOnHideChangedVisibilityState(isVisibility: Boolean) {
        when (onHideChangedState) {
            VisibilityState.DEFAULT -> {
                return
            }
            VisibilityState.ON_CREATE, VisibilityState.VISIBLE, VisibilityState.HIDE -> {
                onHideChangedState = if (isVisibility) {
                    VisibilityState.VISIBLE
                } else {
                    VisibilityState.HIDE
                }
            }
        }
    }

    private fun setUserVisibleHintState(isVisibility: Boolean) {
        onSetUserVisibleHintState = if (isVisibility) {
            VisibilityState.VISIBLE
        } else {
            VisibilityState.HIDE
        }
    }

    private fun setVisibleToUser(isVisibleToUser: Boolean) {
        if (onHideChangedState == VisibilityState.DEFAULT && onSetUserVisibleHintState == VisibilityState.DEFAULT) {
            return
        }
        if (isVisibleToUser == isVisibleToUsers) {
            return
        }
        isVisibleToUsers = isVisibleToUser
        onVisibleToUserChanged(isVisibleToUsers)
    }

    override fun onCreateView() {
        onHideChangedState = VisibilityState.ON_CREATE
    }


    override fun onResume() {
        if (onSetUserVisibleHintState == VisibilityState.VISIBLE) {
            setVisibleToUser(true)
        }

        if (onHideChangedState != VisibilityState.DEFAULT) {
            setVisibleToUser(true)
        }
    }

    override fun onPause() {
        if (onHideChangedState != VisibilityState.HIDE) {
            setUserVisibleHint(false)
            setVisibleToUser(false)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        setUserVisibleHintState(isVisibleToUser)
        setVisibleToUser(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        setOnHideChangedVisibilityState(!hidden)
        setVisibleToUser(!hidden)
    }

    private fun onVisibleToUserChanged(isVisibleToUser: Boolean) {
        listener.invoke(isVisibleToUser)
    }
}
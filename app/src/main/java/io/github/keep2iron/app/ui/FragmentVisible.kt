package com.bana.bananasays.libaspect

/**

 *文件描述：.
 *作者：Created by banana on 2018/11/1.
 *版本号：1.0

 */
interface FragmentVisible {

    fun onCreateView()

    fun onResume()

    fun onPause()

    fun setUserVisibleHint(isVisibleToUser : Boolean )

    fun onHiddenChanged(hidden : Boolean)

}
package io.github.keep2iron.app.module

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.android.widget.BottomTabAdapter
import io.github.keep2iron.android.widget.CompBottomTabLayout
import io.github.keep2iron.app.R

class MainActivity : AbstractActivity<ViewDataBinding>() {

    private val bottomTabLayout: CompBottomTabLayout by FindViewById(R.id.bottomTabLayout)

    private val container: FrameLayout by FindViewById(R.id.container)

    override fun resId(): Int = R.layout.activity_main

    override fun initVariables(savedInstanceState: Bundle?) {
        val list = arrayListOf(
                BottomTabAdapter.TabHolder(
                        colorRes = R.color.b6bcc7,
                        selectColorRes = R.color.ff5b00,
                        title = "Home",
                        iconResId = R.mipmap.ic_home,
                        selIconResId = R.mipmap.ic_home_light,
                        fragment = Fragment()),
                BottomTabAdapter.TabHolder(
                        colorRes = R.color.b6bcc7,
                        selectColorRes = R.color.ff5b00,
                        title = "Favorite",
                        iconResId = R.mipmap.ic_star,
                        selIconResId = R.mipmap.ic_star_light,
                        fragment = Fragment()),
                BottomTabAdapter.TabHolder(
                        colorRes = R.color.b6bcc7,
                        selectColorRes = R.color.ff5b00,
                        title = "Search",
                        iconResId = R.mipmap.ic_search,
                        selIconResId = R.mipmap.ic_search_light,
                        fragment = Fragment()),
                BottomTabAdapter.TabHolder(
                        colorRes = R.color.b6bcc7,
                        selectColorRes = R.color.ff5b00,
                        title = "Notification",
                        iconResId = R.mipmap.ic_notification,
                        selIconResId = R.mipmap.ic_notifications_light,
                        fragment = Fragment())
        )
        val adapter = BottomTabAdapter(this, list)
        bottomTabLayout.setBottomTabAdapter(adapter, container)

    }

}
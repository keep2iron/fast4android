package io.github.keep2iron.app.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import io.github.keep2iron.app.databinding.MainActivityBinding
import io.github.keep2iron.android.comp.widget.BottomTabAdapter
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.core.annotation.StatusColor
import io.github.keep2iron.app.R
import io.github.keep2iron.pitaya.annntation.RouteUri

@Route(path = "/app/main")
@StatusColor(isTrans = false, isDarkMode = false, value = R.color.deep_purple)
class MainActivity : AbstractActivity<MainActivityBinding>() {
    override fun initVariables(savedInstanceState: Bundle?) {
        val list = ArrayList<BottomTabAdapter.TabHolder>()
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "首页",
                R.drawable.ic_home_unselect,
                R.drawable.ic_home_select,
                IndexFragment.getInstance()))
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "分区",
                R.drawable.ic_classification_unselect,
                R.drawable.ic_classification_select,
                ColorFragment.getInstance(R.color.colorPrimary)))
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "时下最热",
                R.drawable.ic_whatshot_unselect,
                R.drawable.ic_whatshot_select,
                ColorFragment.getInstance(R.color.colorAccent)))
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "我",
                R.drawable.ic_face_unselect,
                R.drawable.ic_face_select,
                ColorFragment.getInstance(R.color.colorPrimaryDark)))

        val adapter = BottomTabAdapter(this, list)
        dataBinding.bottomLayout.setBottomTabAdapter(adapter, dataBinding.container)
    }

    override fun getResId(): Int = R.layout.main_activity
}

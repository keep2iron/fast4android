package io.github.keep2iron.app.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.orhanobut.logger.Logger
import io.github.keep2iron.app.databinding.MainActivityBinding
import io.github.keep2iron.android.comp.widget.BottomTabAdapter
import io.github.keep2iron.android.comp.widget.BottomTabLayout
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
                "",
                R.drawable.ic_classification_unselect,
                R.drawable.ic_classification_select,
                RecommendFragment.getInstance()))
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "",
                R.drawable.ic_whatshot_unselect,
                R.drawable.ic_whatshot_select,
                RecommendFragment.getInstance()))
        list.add(BottomTabAdapter.TabHolder(
                R.color.gray,
                R.color.colorPrimary,
                "",
                R.drawable.ic_face_unselect,
                R.drawable.ic_face_select,
                ColorFragment.getInstance(R.color.colorPrimary)))

        val adapter = BottomTabAdapter(this, list)
        dataBinding.bottomLayout.setBottomTabAdapter(adapter, dataBinding.container)
        dataBinding.bottomLayout.addOnTabSelectedListener(object : BottomTabLayout.OnTabChangeListener {
            override fun onTabSelect(position: Int) {
                val gradientColor = intArrayOf(R.color.blue, R.color.purple, R.color.light_green)
                val gradientStateColor = intArrayOf(R.color.deep_blue, R.color.deep_purple, R.color.deep_light_green)

                Logger.e("${position *
                        (dataBinding.bottomLayout.width * 1f / dataBinding.root.width / 3f) +
                        (dataBinding.root.width - dataBinding.bottomLayout.width) * 1f / dataBinding.root.width  / 2}")

                dataBinding.gbvGradientView.animatorNextColor(position *
                        (dataBinding.bottomLayout.width * 1f / dataBinding.root.width / 3f) +
                        (dataBinding.root.width - dataBinding.bottomLayout.width) * 1f / dataBinding.root.width  / 2 +
                        dataBinding.bottomLayout.width * 1f/ dataBinding.root.width / 6,
                        gradientColor[position])
                setStatusColor(gradientStateColor[position])
            }

            override fun onTabUnSelect(position: Int) {
            }

        })
    }

    override fun getResId(): Int = R.layout.main_activity
}

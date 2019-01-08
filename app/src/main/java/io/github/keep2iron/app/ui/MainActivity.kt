package io.github.keep2iron.app.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import io.github.keep2iron.app.databinding.MainActivityBinding
import io.github.keep2iron.android.widget.BottomTabAdapter
import io.github.keep2iron.android.widget.BottomTabLayout
import io.github.keep2iron.android.core.AbstractActivity
import io.github.keep2iron.android.ext.FindViewById
import io.github.keep2iron.android.annotation.StatusColor
import io.github.keep2iron.app.R
import io.github.keep2iron.app.module.movie.MovieFragment
import io.github.keep2iron.app.module.recommend.RecommendFragment
import io.github.keep2iron.app.widget.GradientBackgroundView


@Route(path = "/main/activity")
@StatusColor(isTrans = false, isDarkMode = false, value = R.color.white)
class MainActivity(override val resId: Int = R.layout.main_activity) : AbstractActivity<MainActivityBinding>() {
    var gbvGradientView: GradientBackgroundView by FindViewById(R.id.gbvGradientView)

    override fun initVariables(savedInstanceState: Bundle?) {
        val list = ArrayList<BottomTabAdapter.TabHolder>()
        list.add(BottomTabAdapter.TabHolder(
                colorRes = R.color.gray,
                selectColorRes = R.color.colorPrimary,
                title = "123",
                iconResId = R.drawable.ic_classification_unselect,
                selIconResId = R.drawable.ic_classification_select,
                fragment = MovieFragment.getInstance()))
        list.add(BottomTabAdapter.TabHolder(
                colorRes = R.color.gray,
                selectColorRes = R.color.colorPrimary,
                title = "123",
                iconResId = R.drawable.ic_whatshot_unselect,
                selIconResId = R.drawable.ic_whatshot_select,
                fragment = RecommendFragment.getInstance()))
//        list.add(BottomTabAdapter.TabHolder(
//                colorRes = R.color.gray,
//                selectColorRes = R.color.colorPrimary,
//                title = "123",
//                iconResId = R.drawable.ic_whatshot_unselect,
//                selIconResId = R.drawable.ic_whatshot_select,
//                fragment = MultiTypeFragment()))

//        OverScrollDecoratorHelper.setUpOverScroll(dataBinding.viewPager)

        val adapter = BottomTabAdapter(this, list)
        dataBinding.bottomLayout.setBottomTabAdapter(adapter, dataBinding.viewPager, 1)
        dataBinding.bottomLayout.addOnTabSelectedListener(object : BottomTabLayout.OnTabChangeListener {
            override fun onTabSelect(position: Int) {
                val gradientColor = intArrayOf(R.color.blue, R.color.purple, R.color.light_green)
                val gradientStateColor = intArrayOf(R.color.deep_blue, R.color.deep_purple, R.color.deep_light_green)

                dataBinding.gbvGradientView.animatorNextColor(position *
                        (dataBinding.bottomLayout.width * 1f / dataBinding.root.width / 3f) +
                        (dataBinding.root.width - dataBinding.bottomLayout.width) * 1f / dataBinding.root.width / 2 +
                        dataBinding.bottomLayout.width * 1f / dataBinding.root.width / 6,
                        gradientColor[position])
                setStatusColor(gradientStateColor[position])
            }

            override fun onTabUnSelect(position: Int) {
            }
        })

        dataBinding.viewPager.currentItem = 2
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }
}

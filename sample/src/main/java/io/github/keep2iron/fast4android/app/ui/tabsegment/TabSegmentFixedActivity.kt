package io.github.keep2iron.fast4android.app.ui.tabsegment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import io.github.keep2iron.fast4android.app.R
import io.github.keep2iron.fast4android.app.R.id
import io.github.keep2iron.fast4android.app.databinding.TabSegmentActivityBinding
import io.github.keep2iron.fast4android.arch.AbstractActivity
import io.github.keep2iron.fast4android.arch.swipe.ParallaxBack
import io.github.keep2iron.fast4android.arch.util.findViewByDelegate
import io.github.keep2iron.fast4android.base.FastLogger
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.util.FastStatusBarHelper
import io.github.keep2iron.fast4android.tabsegment.FastTabSegmentLayout
import io.github.keep2iron.fast4android.tabsegment.TabSegmentAdapter
import io.github.keep2iron.fast4android.tabsegment.TabSegmentTextAdapter

@ParallaxBack
class TabSegmentFixedActivity : AbstractActivity<TabSegmentActivityBinding>() {
  private val tabLayout: FastTabSegmentLayout by findViewByDelegate(id.tabLayout)
  private val viewPager: ViewPager by findViewByDelegate(id.viewPager)
  override fun resId(): Int = R.layout.tab_segment_activity
  override fun initVariables(savedInstanceState: Bundle?) {
    FastStatusBarHelper.translucent(this)
    dataBinding.topBarLayout.setup {
      addLeftBackImageButton().setOnClickListener {
        finish()
      }
    }
    val icons = arrayOf(
      R.mipmap.icon_tabbar_component,
      R.mipmap.icon_tabbar_util
      //R.mipmap.icon_tabbar_lab
    )
    viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
      override fun getItem(position: Int): Fragment {
        return TabSegmentFragment.newInstance(
          arrayOf(
            Color.BLACK,
            Color.BLUE,
            Color.RED
          )[(position % 3)]
        )
      }

      override fun getCount(): Int = icons.size
      override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
    }
    tabLayout.tabMode = FastTabSegmentLayout.MODE_SCROLLABLE
//    val tabSegmentAdapter = object: TabSegmentTextAdapter(listOf("文本","文本（123）")){
//      override fun onDrawChildBackground(canvas: Canvas, container: ViewGroup, position: Int, nextPosition: Int, positionOffset: Float, indicatorRect: Rect, indicatorPaint: Paint) {
//        indicatorRect.set(indicatorRect.centerX() - dp2px(20),
//          indicatorRect.top,
//          indicatorRect.centerX() + dp2px(20),
//          indicatorRect.bottom
//        )
//        FastLogger.d("tag","indicatorRect : $indicatorRect ${indicatorRect.centerX()}")
//
//        super.onDrawChildBackground(canvas, container, position, nextPosition, positionOffset, indicatorRect, indicatorPaint)
//      }
//    }
    val tabSegmentAdapter = object : TabSegmentAdapter() {
      val selectedIcons = arrayOf(
        R.mipmap.icon_tabbar_component_selected,
        R.mipmap.icon_tabbar_util_selected
//        R.mipmap.icon_tabbar_lab_selected
      )

      override fun createTab(parentView: ViewGroup, index: Int, selected: Boolean): View {
        return ImageView(parentView.context)
      }

      override fun onTabStateChanged(view: View, index: Int, selected: Boolean) {
        (view as ImageView).apply {
          setImageDrawable(ContextCompat.getDrawable(
            context, if (selected) {
            selectedIcons[index]
          } else {
            icons[index]
          }
          ).also {
            it?.setBounds(0, 0, dp2px(this@TabSegmentFixedActivity, 20), dp2px(this@TabSegmentFixedActivity, 20))
          })
        }
      }

      override fun getItemSize(): Int = icons.size

      override fun onDrawChildBackground(canvas: Canvas, container: ViewGroup, position: Int, nextPosition: Int, positionOffset: Float, indicatorRect: Rect, indicatorPaint: Paint) {
//        super.onDrawChildBackground(canvas, container, position, nextPosition, positionOffset, indicatorRect, indicatorPaint)
        FastLogger.d("tag","indicatorRect : $indicatorRect ${container.getChildAt(0).left} ${container.getChildAt(1).left}")
//        indicatorRect.set(indicatorRect.centerX() - dp2px(20),
//          indicatorRect.top,
//          indicatorRect.centerX() + dp2px(20),
//          indicatorRect.bottom
//        )
//        FastLogger.d("tag","indicatorRect : $indicatorRect ${container.getChildAt(0).left} ${container.getChildAt(0).right} ${container.getChildAt(1).left} ${container.getChildAt(1).right}")
        canvas.drawRect(indicatorRect, indicatorPaint)
      }
    }
    tabLayout.setupWithViewPager(viewPager)
    tabLayout.setAdapter(tabSegmentAdapter)
  }
}
package io.github.keep2iron.bottomnavlayout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 09:30
 */
class FastBottomTabLayout : LinearLayout {
  /**
   * 用于container是ViewPager的时候的position
   */
  private var position: Int = 0

  /**
   * 用于ViewPager的offset参数
   */
  private var positionOffset: Float = 0f

  private lateinit var adapter: BottomTabAdapter
  private var tabIconWidth: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
  private var tabIconHeight: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
  private var tabTextSize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, context.resources.displayMetrics)
  private var tabHeight: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.resources.displayMetrics)
  private var tabDrawablePadding: Int = 0
  private var tabItemMargin: Int = 0
  private var tabItemBackgroundRes: Int = -1
  private lateinit var onTabStateChangedListeners: ArrayList<OnTabChangeListener>

  var container: View? = null

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    setWillNotDraw(false)
    orientation = HORIZONTAL

    val array = resources.obtainAttributes(attrs, R.styleable.FastBottomTabLayout)
    tabTextSize = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_text_size, tabTextSize)
    tabIconWidth = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_icon_width, tabIconWidth)
    tabIconHeight = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_icon_height, tabIconHeight)
    tabDrawablePadding = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_drawable_padding, 0f).toInt()
    tabItemMargin = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_item_margin, 0f).toInt()
    tabItemBackgroundRes = array.getResourceId(R.styleable.FastBottomTabLayout_fast_tab_item_background, -1)
    tabHeight = array.getDimension(R.styleable.FastBottomTabLayout_fast_tab_height, tabHeight)

    array.recycle()
  }

  fun setBottomTabAdapter(adapter: BottomTabAdapter, container: View, defaultPosition: Int = 0) {
    this.adapter = adapter
    adapter.tabs[defaultPosition].fragment?.let { frag ->
      adapter.showingFragment = frag
    }
    adapter.selectPosition = defaultPosition
    adapter.containerView = container

    onTabStateChangedListeners = adapter.onTabStateChangedListeners

    if (container is ViewPager) {
      setWithViewPager(container)
      if (defaultPosition != 0) {
        container.currentItem = defaultPosition
      }
    }

    setViewWithAdapter(adapter, container)

    setTabSelect(defaultPosition)
  }

  fun addOnTabSelectedListener(listener: OnTabChangeListener) {
    adapter.onTabStateChangedListeners.add(listener)
  }

  private fun setTabSelect(position: Int) {
    val tab = adapter.tabs[position]

    if (!tab.isCustom) {
      adapter.setTabSelect(position)
    } else {
      for (listener in adapter.onTabStateChangedListeners) {
        listener.onTabSelect(position)
        listener.onTabUnSelect(adapter.selectPosition)
      }
    }

    this.position = position
    this.positionOffset = 0f
  }

  private fun setViewWithAdapter(adapter: BottomTabAdapter, container: View) {
    for (i in adapter.tabs.indices) {
      val tab = adapter.tabs[i]
      tab.tabIconWidth = tabIconWidth
      tab.tabIconHeight = tabIconHeight
      if (!tab.isCustom) {
        tab.customView = adapter.provideDefaultTextView(
          context,
          tab,
          tabTextSize.toInt(),
          tabHeight,
          tabDrawablePadding,
          i == adapter.selectPosition)
        if (tabItemBackgroundRes != -1) {
          tab.customView.setBackgroundResource(tabItemBackgroundRes)
        }
        tab.customView.setOnClickListener {
          if (i != adapter.selectPosition) {
            if (!tab.isCustom && container !is ViewPager) {
              adapter.setTabSelect(i)
            } else if (!tab.isCustom && container is ViewPager) {
              container.currentItem = i
            } else {
              for (listener in adapter.onTabStateChangedListeners) {
                listener.onTabSelect(i)
                listener.onTabUnSelect(adapter.selectPosition)
              }
            }
          }
        }
      }

      val params = LayoutParams(MATCH_PARENT, MATCH_PARENT)
      params.weight = 1.0f
      params.width = 0
      params.leftMargin = tabItemMargin
      params.rightMargin = tabItemMargin
      tab.customView.layoutParams = params
      addView(tab.customView)

      adapter.onBindTabHolder(i, tab)
    }
  }

  fun setCurrentPosition(position: Int) {
    if (container is ViewPager) {
      val viewPager = container as ViewPager
      viewPager.currentItem = position
    } else {
      setTabSelect(position)
    }
  }

  private fun setWithViewPager(viewPager: ViewPager) {
    if (context !is FragmentActivity) {
      throw IllegalArgumentException(String.format("%s 's context is not FragmentActivity", javaClass.simpleName))
    }
    val manager = (context as FragmentActivity).supportFragmentManager
    viewPager.adapter = BottomTabViewPagerAdapter(manager, adapter.tabs)
    viewPager.offscreenPageLimit = adapter.getItemCount()
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {
      }

      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        this@FastBottomTabLayout.position = position
        this@FastBottomTabLayout.positionOffset = positionOffset
        invalidate()
      }

      override fun onPageSelected(position: Int) {
        if (adapter.selectPosition != position) {
          adapter.tabs[position].select()
          adapter.tabs[adapter.selectPosition].unSelect()

          for (listener in onTabStateChangedListeners) {
            listener.onTabSelect(position)
            listener.onTabUnSelect(adapter.selectPosition)
          }
          adapter.selectPosition = position
        }
      }
    })
  }

  interface OnTabChangeListener {
    /**
     * 当点击tab进行回调该方法 holder中的isEnable不影响该方法的调用
     * @link BottomTabAdapter.Tab
     */
    fun onTabSelect(position: Int)

    /**
     * 当切换tab时调用
     * @param position 上一次选中的position
     */
    fun onTabUnSelect(position: Int)
  }

  class BottomTabViewPagerAdapter(fm: FragmentManager, private val tabs: List<BottomTabAdapter.Tab>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = tabs.size

    override fun getItem(position: Int): Fragment {
      return tabs[position].fragment
        ?: throw IllegalArgumentException("tabs[$position] fragment is null!")
    }
  }
}
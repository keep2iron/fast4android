package io.github.keep2iron.fast4android.tabsegment

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.HorizontalScrollView
import io.github.keep2iron.fast4android.core.util.getAttrColor

/**
 * use this can quick set a bottom tab layoutInflate.
 * for exp:
 * 1.create a list with BottomTabAdapter.
 *
 * ``val list = ArrayList<BottomTabAdapter.TabHolder>()``
 * ``val adapter = BottomTabAdapter(context, list)``
 *
 *  list.add(BottomTabAdapter.TabHolder(
 *           colorRes = R.color.gray,
 *           selectColorRes = R.color.colorPrimary,
 *           title = "123",
 *           iconResId = R.drawable.ic_classification_unselect,
 *           selIconResId = R.drawable.ic_classification_select,
 *           fragment = YourFragment()))
 *
 * 2.new a BottomTabAdapter.TabHolder add tab with list
 *
 * if you have 4 tab item,you could use this method 4 times
 *
 * 3.set adapter with @see io.github.keep2iron.android.widget.CompBottomTabLayout
 *
 * val bottomTabLayout = findViewById<CompBottomTabLayout>(R.id.bottomTabLayout)
 * bottomTabLayout.setBottomTabAdapter(adapter = adapter,container = yourContainerView,defaultPosition = 0)
 *
 * if container is ViewPager it auto can scroll with ViewPager well.
 * if container is FrameLayout it only click tab to switch tab.
 *
 * @see android.widget.LinearLayout
 * @author keep2iron
 */
class FastTabSegmentLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = R.attr.FastTabSegmentLayoutStyle
) : HorizontalScrollView(context, attrs, defStyleAttr) {

  companion object {
    // mode 自适应宽度/均分
    const val MODE_FIXED = 0
    // mode 可滚动
    const val MODE_SCROLLABLE = 1

    // icon 位置
    const val ICON_POSITION_LEFT = 0
    const val ICON_POSITION_TOP = 1
    const val ICON_POSITION_RIGHT = 2
    const val ICON_POSITION_BOTTOM = 3
    // 没有图标
    const val ICON_NONE = -1

    // 设置int的一些属性的默认值 init方法会将其覆盖
    private const val DEFAULT_DIMEN_VALUE = 0
  }

  interface OnTabSelectedListener {

    /**
     * 当tab选中时触发
     *
     * @param index 选中的tab的下标
     * @param reselected 第一次从不选中到选中时为false,当选中状态时再次选中为true
     */
    fun onTabSelected(index: Int, reselected: Boolean) {}

    /**
     * 当tab从选中时到不选中时触发
     *
     * @param index 被取消选中状态的index下标
     */
    fun onTabUnselected(index: Int) {}

    /**
     * 当tab双击时触发
     *
     * @param index 双击时的下标
     */
    fun onTabDoubleTap(index: Int) {}

  }

  /**
   * selected changed listener
   */
  val selectedListeners = ArrayList<OnTabSelectedListener>()
  /**
   * 默认状态的文字是否加粗
   */
  var isNormalTabBold = false
  /**
   * 选中状态的文字是否加粗
   */
  var isSelectedTabBold = true
  /**
   * 字体
   */
  var typeface: Typeface? = null
  /**
   * 指示器高度
   */
  var indicatorHeight: Int = DEFAULT_DIMEN_VALUE
  /**
   * 指示器颜色
   */
  var indicatorColor: Int = DEFAULT_DIMEN_VALUE



  /**
   * 用于container是ViewPager的时候的position
   */
  private var position: Int = 0
  /**
   * 用于ViewPager的offset参数
   */
  private var positionOffset: Float = 0f

  //  private lateinit var adapter: BottomTabAdapter
  private var tabIconWidth: Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
  private var tabIconHeight: Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
  private var tabTextSize: Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, context.resources.displayMetrics)
  private var tabHeight: Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.resources.displayMetrics)

  private var tabDrawablePadding: Int = 0
  private var tabItemMargin: Int = 0
  private var tabItemBackgroundRes: Int = -1
//  private lateinit var onTabStateChangedListeners: ArrayList<OnTabChangeListener>

  /**
   * 选中颜色
   */
  var itemSelectTextColor: Int = 0
  /**
   * 默认颜色
   */
  var itemTextColor: Int = 0

  var container: View? = null

  init {
    val defaultNormal = context.getAttrColor(R.attr.fast_config_color_gray_5)
    val selectedColor = context.getAttrColor(android.R.attr.textColorPrimary)

    val array =
      context.obtainStyledAttributes(attrs, R.styleable.FastTabSegmentLayout, defStyleAttr, 0)
    array.getDimensionPixelSize(
      R.styleable.FastTabSegmentLayout_fast_tab_indicator_height,
      resources.getDimensionPixelSize(R.dimen.fast_tab_segment_indicator_height)
    )

//    tabTextSize =
//      array.getDimension(R.styleable.CompBottomTabLayout_comp_tabItem_textSize, tabTextSize)
//    tabIconWidth =
//      array.getDimension(R.styleable.CompBottomTabLayout_comp_tabItem_drawableWidth, tabIconWidth)
//    tabIconHeight =
//      array.getDimension(R.styleable.CompBottomTabLayout_comp_tabItem_drawableHeight, tabIconHeight)
//    tabDrawablePadding =
//      array.getDimension(R.styleable.CompBottomTabLayout_comp_tabItem_drawablePadding, 0f).toInt()
//    tabItemMargin =
//      array.getDimension(R.styleable.CompBottomTabLayout_comp_tabItem_margin, 0f).toInt()
//    tabItemBackgroundRes =
//      array.getResourceId(R.styleable.CompBottomTabLayout_comp_tabItem_background, -1)
//    tabHeight = array.getDimension(R.styleable.CompBottomTabLayout_comp_tabLayout_height, tabHeight)

    array.recycle()
  }

//  fun setBottomTabAdapter(adapter: BottomTabAdapter, container: View, defaultPosition: Int = 0) {
//    this.adapter = adapter
//    adapter.tabs[defaultPosition].fragment?.let { frag ->
//      adapter.showingFragment = frag
//    }
//    adapter.selectPosition = defaultPosition
//    adapter.containerView = container
//
//    onTabStateChangedListeners = adapter.onTabStateChangedListeners
//
//    if (container is androidx.viewpager.widget.ViewPager) {
//      setWithViewPager(container)
//      if (defaultPosition != 0) {
//        container.currentItem = defaultPosition
//      }
//    }
//
//    setViewWithAdapter(adapter, container)
//
//    setTabSelect(defaultPosition)
//  }
//
//  /**
//   * add tab select listener
//   */
//  fun addOnTabSelectedListener(listener: OnTabChangeListener) {
//    adapter.onTabStateChangedListeners.add(listener)
//  }
//
//  /**
//   * when container view is android.view.FrameLayout it can switch to [position] fragment
//   */
//  private fun setTabSelect(position: Int) {
//    val tab = adapter.tabs[position]
//
//    if (!tab.isCustom) {
//      adapter.setTabSelect(position)
//    } else {
//      for (listener in adapter.onTabStateChangedListeners) {
//        listener.onTabSelect(position)
//        listener.onTabUnSelect(adapter.selectPosition)
//      }
//    }
//
//    this.position = position
//    this.positionOffset = 0f
//  }
//
//  private fun setViewWithAdapter(adapter: BottomTabAdapter, container: View) {
//    for (i in 0 until adapter.tabs.size) {
//      val tab = adapter.tabs[i]
//      tab.tabIconWidth = tabIconWidth
//      tab.tabIconHeight = tabIconHeight
//      if (!tab.isCustom) {
//        tab.customView = adapter.provideDefaultTextView(
//          context,
//          tab,
//          tabIconWidth,
//          tabIconHeight,
//          tabTextSize.toInt(),
//          tabHeight,
//          tabDrawablePadding,
//          i == adapter.selectPosition
//        )
//        if (tabItemBackgroundRes != -1) {
//          tab.customView.setBackgroundResource(tabItemBackgroundRes)
//        }
//        tab.customView.setOnClickListener {
//          if (i != adapter.selectPosition) {
//            if (!tab.isCustom && container !is androidx.viewpager.widget.ViewPager) {
//              adapter.setTabSelect(i)
//            } else if (!tab.isCustom && container is androidx.viewpager.widget.ViewPager) {
//              container.currentItem = i
//            } else {
//              for (listener in adapter.onTabStateChangedListeners) {
//                listener.onTabSelect(i)
//                listener.onTabUnSelect(adapter.selectPosition)
//              }
//            }
//          }
//        }
//      }
//
//      val params = LayoutParams(MATCH_PARENT, MATCH_PARENT)
//      params.weight = 1.0f
//      params.width = 0
//      params.leftMargin = tabItemMargin
//      params.rightMargin = tabItemMargin
//      tab.customView.layoutParams = params
//      addView(tab.customView)
//
//      adapter.onBindTabHolder(i, tab)
//    }
//  }
//
//  /**
//   * Switch to the container at the [position] location
//   *
//   * @param position position in the BottomTabAdapter tabs
//   */
//  fun setCurrentPosition(position: Int) {
//    if (container is androidx.viewpager.widget.ViewPager) {
//      val viewPager = container as androidx.viewpager.widget.ViewPager
//      viewPager.currentItem = position
//    } else {
//      setTabSelect(position)
//    }
//  }
//
//  /**
//   * if container is ViewPager
//   *
//   * @param viewPager container view
//   */
//  private fun setWithViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
//    if (context !is androidx.fragment.app.FragmentActivity) {
//      throw IllegalArgumentException(
//        String.format(
//          "%s 's context is not FragmentActivity",
//          javaClass.simpleName
//        )
//      )
//    }
//    val manager = (context as androidx.fragment.app.FragmentActivity).supportFragmentManager
//    viewPager.adapter = BottomTabViewPagerAdapter(manager, adapter.tabs)
//    viewPager.offscreenPageLimit = adapter.getItemCount()
//    viewPager.addOnPageChangeListener(object :
//      androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//      override fun onPageScrollStateChanged(state: Int) {
//      }
//
//      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        this@CompBottomTabLayout.position = position
//        this@CompBottomTabLayout.positionOffset = positionOffset
//        invalidate()
//      }
//
//      override fun onPageSelected(position: Int) {
//        if (adapter.selectPosition != position) {
//          adapter.tabs[position].select()
//          adapter.tabs[adapter.selectPosition].unSelect()
//
//          for (listener in onTabStateChangedListeners) {
//            listener.onTabSelect(position)
//            listener.onTabUnSelect(adapter.selectPosition)
//          }
//          adapter.selectPosition = position
//        }
//      }
//    })
//  }
//
//  interface OnTabChangeListener {
//    /**
//     * on tab select
//     *
//     * @param position select position
//     */
//    fun onTabSelect(position: Int)
//
//    /**
//     * on tab not select
//     *
//     * @param position not select position
//     */
//    fun onTabUnSelect(position: Int)
//  }
//
//  internal class BottomTabViewPagerAdapter(
//    fm: androidx.fragment.app.FragmentManager,
//    private val tabs: ArrayList<BottomTabAdapter.TabHolder>
//  ) : androidx.fragment.app.FragmentPagerAdapter(fm) {
//
//    override fun getCount(): Int = tabs.size
//
//    override fun getItem(position: Int): androidx.fragment.app.Fragment {
//      return tabs[position].fragment
//        ?: throw IllegalArgumentException("BottomTabHolder`s fragment is null,you must set a fragment in tabs[$position]")
//    }
//  }
}
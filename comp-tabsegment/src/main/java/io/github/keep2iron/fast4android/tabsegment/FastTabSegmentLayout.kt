package io.github.keep2iron.fast4android.tabsegment

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.github.keep2iron.fast4android.core.util.dp2px
import io.github.keep2iron.fast4android.core.util.getAttrColor
import kotlin.LazyThreadSafetyMode.NONE

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
    const val MODE_FIXED = 1
    // mode 可滚动
    const val MODE_SCROLLABLE = 0

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
     */
    fun onTabSelected(index: Int) {}

    /**
     * 当tab从选中时到不选中时触发
     *
     * @param index 被取消选中状态的index下标
     */
    fun onTabUnselected(index: Int) {}

  }

  /**
   * selected changed listener
   */
  val selectedListeners = ArrayList<OnTabSelectedListener>()

  var tabIconWidth: Int = dp2px(10)
  var tabIconHeight: Int = dp2px(10)
  var tabTextSize: Int = resources.getDimensionPixelSize(R.dimen.fast_tab_segment_text_size)
  var tabMode: Int = MODE_FIXED
  /**
   * tab的两边间隙 tabMode == MODE_SCROLLABLE 时有效
   */
  var tabSpacing: Int = dp2px(10)
  var tabPosition: Int = ICON_POSITION_LEFT
  //  private var tabDrawablePadding: Int = 0
  var tabItemMargin: Int = 0
  private val tabContainer by lazy(NONE) {
    val linearLayout = Container(context)
    linearLayout
  }

  private var adapter: TabSegmentAdapter? = null

  var viewPager: ViewPager? = null
  /**
   * 用于ViewPager的offset参数
   */
  var positionOffset: Float = 0f

  val indicatorRect = Rect()
  /**
   * 指示器高度
   */
  var indicatorHeight: Int =
    resources.getDimensionPixelSize(R.dimen.fast_tab_segment_indicator_height)
  /**
   * 指示器颜色
   */
  var indicatorColor: Int = DEFAULT_DIMEN_VALUE
    set(value) {
      field = value
      indicatorPaint.color = value
    }
  val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

  val indicatorDrawable: Drawable? = null

  /**
   * 用于container是ViewPager的时候的position
   */
  private var position: Int = 0

  /**
   * 选中颜色
   */
  var itemSelectTextColor: Int = 0
    set(value) {
      field = value
      adapter?.selectColor = field
    }
  /**
   * 默认颜色
   */
  var itemTextColor: Int = 0
    set(value) {
      field = value
      adapter?.normalColor = field
    }

  init {
    val array =
      context.obtainStyledAttributes(attrs, R.styleable.FastTabSegmentLayout, defStyleAttr, 0)
    indicatorHeight = array.getDimensionPixelSize(
      R.styleable.FastTabSegmentLayout_fast_tab_indicator_height,
      indicatorHeight
    )
    tabTextSize =
      array.getDimensionPixelSize(R.styleable.FastTabSegmentLayout_android_textSize, tabTextSize)
    tabMode = array.getInt(R.styleable.FastTabSegmentLayout_fast_tab_mode, tabMode)
    tabPosition = array.getInt(R.styleable.FastTabSegmentLayout_fast_tab_icon_position, tabPosition)
    itemTextColor = array.getColor(
      R.styleable.FastTabSegmentLayout_fast_tab_normal_color,
      context.getAttrColor(R.attr.fast_config_color_gray_5)
    )
    itemSelectTextColor = array.getColor(
      R.styleable.FastTabSegmentLayout_fast_tab_selected_color,
      context.getAttrColor(R.attr.colorPrimary)
    )
    array.recycle()

    indicatorColor = itemSelectTextColor
  }

  fun setAdapter(adapter: TabSegmentAdapter) {
    this.adapter = adapter
    adapter.mObservable.registerObserver(SegmentLayoutAdapterDataObserver(this))
    adapter.normalColor = itemTextColor
    adapter.selectColor = itemSelectTextColor

    addView(tabContainer, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))

    for (i in 0 until adapter.getItemSize()) {
      val tabView = adapter.createTab(this, i, false)
      tabContainer.addView(tabView, generateDefaultLayoutParams())
    }
    tabContainer.requestLayout()
  }

  fun setupWithViewPager(viewPager: ViewPager, defaultPosition: Int = 0) {
    this.viewPager = viewPager
    viewPager.addOnPageChangeListener(tabContainer)
    viewPager.adapter?.registerDataSetObserver(object : DataSetObserver() {
      override fun onChanged() {
        this@FastTabSegmentLayout.adapter?.notifyDataSetChanged()
      }
    })
    this.position = defaultPosition
  }

  private class SegmentLayoutAdapterDataObserver(val view: FastTabSegmentLayout) :
    AdapterDataObserver() {
    override fun onChanged() {
      view.requestLayout()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
      onItemRangeChanged(positionStart, itemCount, null)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
      view.requestLayout()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
      onItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
      onItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
      onItemRangeChanged(fromPosition, itemCount)
    }
  }

  private inner class Container @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
  ) : ViewGroup(context, attrs, defStyleAttr), OnPageChangeListener, OnClickListener {

    override fun onClick(v: View) {
      val index = indexOfChild(v)
      selectedListeners.forEach {
        it.onTabUnselected(position)
      }
      selectedListeners.forEach {
        it.onTabSelected(index)
      }
      adapter?.let {
        it.onBindTab(tabContainer.getChildAt(position), position, false)
        it.onBindTab(tabContainer.getChildAt(index), index, true)
      }
      position = index
      viewPager?.currentItem = position
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
      val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
      val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
      val width: Int

      if (adapter == null) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "adapter is null onMeasure is skip")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        return
      }

      if (tabMode == MODE_FIXED) {
        width = parentWidth / adapter!!.getItemSize()
        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec)
        setMeasuredDimension(parentWidth, parentHeight)
      } else if (tabMode == MODE_SCROLLABLE) {
        var totalWidth = 0
        measureChildren(
          MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST),
          heightMeasureSpec
        )

        for (i in 0 until childCount) {
          val childView = getChildAt(i)
          if (childView.visibility == View.VISIBLE) {
            totalWidth += getChildAt(i).measuredWidth + tabSpacing
          }
        }
        if (totalWidth > 0) {
          totalWidth -= tabSpacing
        }

        setMeasuredDimension(totalWidth, parentHeight)
      }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
      if (adapter == null) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "adapter is null onLayout is skip")
        return
      }

      var lastLeft = l
      for (i in 0 until adapter!!.getItemSize()) {
        val childView = getChildAt(i)
        val left = if (i == 0 || tabMode == MODE_FIXED) {
          paddingLeft + lastLeft
        } else {
          paddingLeft + lastLeft + tabSpacing
        }
        val top = paddingTop
        val right = left + childView.measuredWidth
        val bottom = childView.measuredHeight
        childView.layout(left, top, right, bottom)
        childView.setOnClickListener(this)
        adapter!!.onBindTab(childView, i, position == i)

        lastLeft = right
      }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      this@FastTabSegmentLayout.positionOffset = positionOffset

      val nextPosition = if (positionOffset > 0 && positionOffset < 1) {
        (position + positionOffset + 1).toInt()
      } else {
        position
      }

      if (childCount > 0) {
        val view = getChildAt(position)
        val nextView = getChildAt(nextPosition)
        indicatorRect.left = view.left + ((nextView.left - view.left) * positionOffset).toInt()
        indicatorRect.right = indicatorRect.left + view.width
        invalidate()
      }
    }

    override fun onPageSelected(position: Int) {
      val selectedTab = tabContainer.getChildAt(position)
      adapter?.let {
        it.onBindTab(
          tabContainer.getChildAt(this@FastTabSegmentLayout.position),
          this@FastTabSegmentLayout.position,
          false
        )
        it.onBindTab(selectedTab, position, true)
      }
      if (this@FastTabSegmentLayout.scrollX + this@FastTabSegmentLayout.width < selectedTab.right) {
        this@FastTabSegmentLayout.smoothScrollBy(
          selectedTab.right - this@FastTabSegmentLayout.width - this@FastTabSegmentLayout.scrollX,
          0
        )
      } else if (this@FastTabSegmentLayout.scrollX > selectedTab.left) {
        this@FastTabSegmentLayout.smoothScrollBy(
          selectedTab.left - this@FastTabSegmentLayout.scrollX,
          0
        )
      }
      this@FastTabSegmentLayout.position = position
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun dispatchDraw(canvas: Canvas) {
      super.dispatchDraw(canvas)

      indicatorRect.bottom = height - paddingBottom
      indicatorRect.top = indicatorRect.bottom - indicatorHeight

      canvas.drawRect(indicatorRect, indicatorPaint)
    }
  }
}
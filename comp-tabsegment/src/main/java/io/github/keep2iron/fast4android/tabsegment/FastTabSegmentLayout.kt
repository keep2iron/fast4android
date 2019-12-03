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
import io.github.keep2iron.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.base.util.getAttrColor
import kotlin.LazyThreadSafetyMode.NONE

class FastTabSegmentLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.FastTabSegmentLayoutStyle
) : HorizontalScrollView(context, attrs, defStyleAttr), AdapterDataObserver {
    companion object {
        // mode 自适应宽度/均分
        const val MODE_FIXED = 1
        // mode 可滚动
        const val MODE_SCROLLABLE = 0
        // // icon 位置
        // const val ICON_POSITION_LEFT = 0
        // const val ICON_POSITION_TOP = 1
        // const val ICON_POSITION_RIGHT = 2
        // const val ICON_POSITION_BOTTOM = 3
        // // 没有图标
        // const val ICON_NONE = -1
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
    // var tabIconWidth: Int = dp2px(context, 10)
    // var tabIconHeight: Int = dp2px(context, 10)
    var tabTextSize: Int = resources.getDimensionPixelSize(R.dimen.fast_tab_segment_text_size)
    var tabMode: Int = MODE_FIXED
    /**
     * tab的两边间隙 tabMode == MODE_SCROLLABLE 时有效
     */
    var tabSpacing: Int = dp2px(context, 10)
    // var tabPosition: Int = ICON_POSITION_LEFT
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
        // tabPosition = array.getInt(R.styleable.FastTabSegmentLayout_fast_tab_icon_position, tabPosition)
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
        adapter.mObservable.registerObserver(this)
        adapter.normalColor = itemTextColor
        adapter.selectColor = itemSelectTextColor

        if (tabMode == MODE_SCROLLABLE) {
            addView(tabContainer, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        } else {
            addView(tabContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        for (i in 0 until adapter.getItemSize()) {
            val tabView = adapter.createTab(tabContainer, i, false)
            tabContainer.addView(tabView, generateDefaultLayoutParams())
        }
//        tabContainer.requestLayout()
        requestLayout()

        if (viewPager != null && viewPager?.currentItem != position) {
            viewPager?.currentItem = position
        }

        adapter.onAttachTabSegmentLayout(this)
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
        //如果adapter设置过了
        if (adapter != null) {
            viewPager.setCurrentItem(position, false)
        }
    }

    fun childTabAt(index: Int): View {
        return tabContainer.getChildAt(index)
    }

    override fun onChanged() {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onChanged")
        val adapterItemSize = adapter?.getItemSize() ?: 0
        if (adapter != null && adapterItemSize != tabContainer.childCount) {
            tabContainer.removeAllViews()
            for (i in 0 until adapter!!.getItemSize()) {
                val tabView = adapter!!.createTab(tabContainer, i, false)
                tabContainer.addView(
                        tabView,
                        ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                )
            }

            viewPager?.adapter?.notifyDataSetChanged()
            if (adapterItemSize != 0 && position >= adapterItemSize) {
                this.position = adapterItemSize - 1
                viewPager?.currentItem = adapterItemSize - 1
            }
        }
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onItemRangeChanged...")
        onItemRangeChanged(positionStart, itemCount, null)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onItemRangeChanged...")
        tabContainer.requestLayout()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onItemRangeInserted...")
        onItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onItemRangeRemoved...")
        onItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        Log.d(FastTabSegmentLayout::class.java.simpleName, "onItemRangeMoved...")
        onItemRangeChanged(fromPosition, itemCount)
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
                it.onTabStateChanged(tabContainer.getChildAt(position), position, false)
                it.onTabStateChanged(tabContainer.getChildAt(index), index, true)
            }
            position = index
            viewPager?.currentItem = position
        }

        private fun requireAdapter(): TabSegmentAdapter {
            return adapter ?: throw IllegalArgumentException("adapter is null")
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val parentWidth = if (MeasureSpec.getSize(widthMeasureSpec) == 0) {
                (parent as FastTabSegmentLayout).measuredWidth
            } else {
                MeasureSpec.getSize(widthMeasureSpec)
            }
            val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
            val width: Int

            if (adapter == null) {
                Log.d(FastTabSegmentLayout::class.java.simpleName, "adapter is null onMeasure is skip")
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                return
            }

            if (tabMode == MODE_FIXED) {
                val widthMode = MeasureSpec.getMode(widthMeasureSpec)
                val nonNullAdapter = requireAdapter()
                width = if (nonNullAdapter.getItemSize() <= 0) {
                    0
                } else {
                    parentWidth / nonNullAdapter.getItemSize()
                }

                when (widthMode) {
                    MeasureSpec.EXACTLY -> {
                        measureChildren(
                                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                                heightMeasureSpec
                        )
                        setMeasuredDimension(parentWidth, parentHeight)
                    }
                    MeasureSpec.UNSPECIFIED,
                    MeasureSpec.AT_MOST -> {
                        measureChildren(
                                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                                heightMeasureSpec
                        )
                        var totalWidth = 0
                        for (i in 0 until childCount) {
                            val child = getChildAt(i)
                            if (child.visibility != View.GONE) {
                                totalWidth += child.measuredWidth
                            }
                        }
                        setMeasuredDimension(totalWidth, parentHeight)
                    }
                }
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
                if (changed) {
                    childView.setOnClickListener(this)
                    adapter!!.onTabStateChanged(childView, i, position == i)
                }
                lastLeft = right
            }

            if (position < childCount) {
                if (indicatorHeight > 0) {
                    val view = getChildAt(position)
                    indicatorRect.left = view.left
                    indicatorRect.right = indicatorRect.left + view.width
                }
            } else if (position > childCount && childCount != 0) {
                onPageSelected(childCount - 1)
                if (indicatorHeight > 0) {
                    //如果当前位置已经被删除了
                    val view = getChildAt(position)
                    indicatorRect.left = view.left
                    indicatorRect.right = indicatorRect.left + view.width
                    invalidate()
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            this@FastTabSegmentLayout.positionOffset = positionOffset
            val nextPosition = if (positionOffset > 0 && positionOffset < 1) {
                (position + positionOffset + 1).toInt()
            } else {
                position
            }

            if (position < childCount && nextPosition < childCount && indicatorHeight > 0) {
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
                //https://stackoverflow.com/questions/10396321/remove-fragment-page-from-viewpager-in-android
                //当比如由于viewPager的下表为3时，但是这时候只有两个fragment，那么viewPager会自动切换到最后的那个fragment，所以
                //要判断一下
                if (this@FastTabSegmentLayout.position < tabContainer.childCount) {
                    it.onTabStateChanged(
                            tabContainer.getChildAt(this@FastTabSegmentLayout.position),
                            this@FastTabSegmentLayout.position,
                            false
                    )
                }
                it.onTabStateChanged(selectedTab, position, true)
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
            if (indicatorHeight > 0) {
                indicatorRect.bottom = height - paddingBottom
                indicatorRect.top = indicatorRect.bottom - indicatorHeight - 100
            }

            canvas.drawRect(indicatorRect, indicatorPaint)
            super.dispatchDraw(canvas)
        }
    }
}
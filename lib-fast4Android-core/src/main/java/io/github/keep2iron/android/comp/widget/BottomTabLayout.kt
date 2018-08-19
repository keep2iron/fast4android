package io.github.keep2iron.android.comp.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import io.github.keep2iron.android.R
import io.github.keep2iron.android.core.dimen
import io.github.keep2iron.android.core.dp2px

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/02 09:30
 */
class BottomTabLayout : LinearLayout {
    /**
     * 用于container是ViewPager的时候的position
     */
    private var position: Int = 0
    /**
     * 用于ViewPager的offset参数
     */
    private var positionOffset: Float = 0f

    private lateinit var adapter: BottomTabAdapter
    private var tabIconWidth: Int = dp2px(10)
    private var tabIconHeight: Int = dp2px(10)
    private var tabTextSize: Float = dp2px(10).toFloat()
    private var tabDrawablePadding: Int = 0
    private var tabItemMargin: Int = 0
    private lateinit var onTabStateChangedListeners: ArrayList<OnTabChangeListener>

    var container: View? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        orientation = HORIZONTAL

        val array = resources.obtainAttributes(attrs, R.styleable.BottomTabLayout)
        tabTextSize = array.getDimension(R.styleable.BottomTabLayout_tab_text_size, tabTextSize)
        tabIconWidth = array.getDimension(R.styleable.BottomTabLayout_tab_icon_width, tabIconWidth.toFloat()).toInt()
        tabIconHeight = array.getDimension(R.styleable.BottomTabLayout_tab_icon_height, tabIconHeight.toFloat()).toInt()
        tabDrawablePadding = array.getDimension(R.styleable.BottomTabLayout_tab_drawable_padding, 0f).toInt()
        tabItemMargin = array.getDimension(R.styleable.BottomTabLayout_tab_item_margin, 0f).toInt()
        array.recycle()
    }

    fun setBottomTabAdapter(adapter: BottomTabAdapter, container: View, defaultPosition: Int = 0) {
        this.adapter = adapter
        adapter.showingFragment = adapter.tabs[defaultPosition].fragment!!
        adapter.selectPosition = defaultPosition
        adapter.containerView = container

        onTabStateChangedListeners = adapter.onTabStateChangedListeners

        if (container is ViewPager) {
            setWithViewPager(container)
            if(defaultPosition != 0) {
                container.currentItem = defaultPosition
            }
        }

        setViewWithAdapter(adapter, container)

        setTabSelect(defaultPosition)
    }

    fun addOnTabSelectedListener(listener: BottomTabLayout.OnTabChangeListener) {
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
        invalidate()
    }

    private fun setViewWithAdapter(adapter: BottomTabAdapter, container: View) {
        for (i in 0 until adapter.tabs.size) {
            val tab = adapter.tabs[i]
            tab.tabIconWidth = tabIconWidth
            tab.tabIconHeight = tabIconHeight
            if (!tab.isCustom) {
                tab.customView = adapter.provideDefaultTextView(
                        context,
                        tab,
                        tabIconWidth,
                        tabIconHeight,
                        tabTextSize.toInt(),
                        tabDrawablePadding,
                        i == adapter.selectPosition)
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
                this@BottomTabLayout.position = position
                this@BottomTabLayout.positionOffset = positionOffset
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
         * @link BottomTabAdapter.TabHolder
         */
        fun onTabSelect(position: Int)

        /**
         * 当切换tab时调用
         * @param position 上一次选中的position
         */
        fun onTabUnSelect(position: Int)
    }

    inner class BottomTabViewPagerAdapter(fm: FragmentManager?, private val tabs: ArrayList<BottomTabAdapter.TabHolder>) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int = tabs.size

        override fun getItem(position: Int): Fragment = tabs[position].fragment!!
    }

//    override fun onDraw(canvas: Canvas) {
//        val paint = Paint()
//        paint.color = Color.WHITE
//
//        val itemWith = width * 1f / adapter.tabs.size
//        val startX = itemWith * position + itemWith * positionOffset
//        val endX = startX + itemWith
//
//        canvas.drawRect(startX, (height - 10).toFloat(), endX, height.toFloat(), paint)
//
//    }
}
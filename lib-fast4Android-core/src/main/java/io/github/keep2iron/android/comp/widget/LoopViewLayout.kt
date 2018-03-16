package io.github.keep2iron.android.comp.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.vlayout.extend.InnerRecycledViewPool
import io.github.keep2iron.android.comp.adapter.RecyclerViewHolder
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.orhanobut.logger.Logger
import io.github.keep2iron.android.R
import io.github.keep2iron.android.core.dp2px


/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/08 13:53
 *
 * 可以无限轮播的布局，里面包含了一个ViewPager和一个LinearLayout作为指示器
 */
class LoopViewLayout : FrameLayout {
    lateinit var adapter: WrapperPagerAdapter<out RecyclerViewHolder>
    private var viewPager: NoScrollViewPager
    private var indicators: LinearLayout

    /**
     * 这些都是默认配置
     */
    private var indicatorUnselectResId: Int = 0
    private var indicatorSelectResId: Int = 0
    private var indicatorWidth: Int = dp2px(10)
    private var indicatorHeight: Int = dp2px(10)
    private var indicatorMargin: Int = dp2px(10)

    private var onPageChangedListener: ViewPager.OnPageChangeListener? = null
    private val indicatorImages: ArrayList<ImageView> = ArrayList()
    /**
     * 当前选中position
     */
    var currentPosition: Int = 1

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_banner, this, true)
        viewPager = findViewById(R.id.vpViewPager)
        viewPager.setScroll(true)
        indicators = findViewById(R.id.llIndicatorContainer)

        val array = resources.obtainAttributes(attrs, R.styleable.LoopViewLayout)
        indicatorSelectResId = array.getResourceId(R.styleable.LoopViewLayout_indicator_drawable_select, R.drawable.shape_gray_radius)
        indicatorUnselectResId = array.getResourceId(R.styleable.LoopViewLayout_indicator_drawable_unselect, R.drawable.shape_white_radius)
        indicatorWidth = array.getDimensionPixelSize(R.styleable.LoopViewLayout_indicator_drawable_width, indicatorWidth)
        indicatorHeight = array.getDimensionPixelSize(R.styleable.LoopViewLayout_indicator_drawable_height, indicatorHeight)
        indicatorMargin = array.getDimensionPixelSize(R.styleable.LoopViewLayout_indicator_drawable_margin, indicatorMargin)
        array.recycle()

        initOnPageChangedListener()
    }

    private fun initOnPageChangedListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                onPageChangedListener?.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager.SCROLL_STATE_IDLE ->
                        if (currentPosition == 0) {//No operation
                            viewPager.setCurrentItem(toRealPosition(adapter.count), false)
                        } else if (currentPosition == adapter.count - 1) {
                            viewPager.setCurrentItem(1, false)
                        }
                    ViewPager.SCROLL_STATE_DRAGGING ->
                        if (currentPosition == adapter.count - 1) {//start Sliding
                            viewPager.setCurrentItem(1, false)
                        } else if (currentPosition == 0) {
                            viewPager.setCurrentItem(toRealPosition(adapter.count), false)
                        }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageChangedListener?.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                val realPosition = toRealPosition(position)

                (indicators.getChildAt(currentPosition) as ImageView).setImageResource(indicatorUnselectResId)
                (indicators.getChildAt(realPosition) as ImageView).setImageResource(indicatorSelectResId)

                currentPosition = realPosition
                onPageChangedListener?.onPageSelected(realPosition)
            }
        })
    }

    /**
     * 创建指示器
     */
    fun createIndicator() {
        indicators.removeAllViews()

        for (i in 0 until adapter.getRealCount()) {
            val imageView = ImageView(context)
            imageView.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                val linearParams = LinearLayout.LayoutParams(indicatorWidth, indicatorHeight)
                linearParams.leftMargin = indicatorMargin
                linearParams.rightMargin = indicatorMargin
                layoutParams = linearParams
                setImageResource(indicatorUnselectResId)
                indicators.addView(imageView)
            }
        }
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    fun toRealPosition(position: Int): Int {
        var realPosition = (position - 1) % adapter.getRealCount()
        if (realPosition < 0)
            realPosition += adapter.getRealCount()
        return realPosition
    }

    fun setPageMargin(marginPixels: Int) {
        viewPager.pageMargin = marginPixels
    }

    /**
     * 设置RecyclerView的Adapter，主要是因为这个adapter可以和RecyclerView进行结合，共用一个复用池
     *
     * @param recycleAdapter RecyclerView的Adapter
     * @param pool 使用
     */
    fun <T : RecyclerViewHolder> setAdapter(recycleAdapter: RecyclerView.Adapter<T>,
                                            pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()) {
        viewPager.removeAllViews()
        this.adapter = WrapperPagerAdapter(recycleAdapter, pool)
        recycleAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                this@LoopViewLayout.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                createIndicator()
                this@LoopViewLayout.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any) {
                // fallback to onItemRangeChanged(positionStart, itemCount) if app
                // does not override this method.
                onItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                createIndicator()
                this@LoopViewLayout.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                createIndicator()
                this@LoopViewLayout.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                createIndicator()
                this@LoopViewLayout.adapter.notifyDataSetChanged()
            }
        })
        createIndicator()
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }

    class WrapperPagerAdapter<T : RecyclerViewHolder>(val adapter: RecyclerView.Adapter<T>,
                                                      pool: RecyclerView.RecycledViewPool) : PagerAdapter() {
        private var pool: InnerRecycledViewPool

        init {
            if (pool is InnerRecycledViewPool) {
                this.pool = pool
            } else {
                this.pool = InnerRecycledViewPool(pool)
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var realPosition = (position - 1) % adapter.itemCount
            if (realPosition < 0)
                realPosition += adapter.itemCount

            val itemViewType = adapter.getItemViewType(realPosition)
            var holder = pool.getRecycledView(itemViewType)
            if (holder == null) {
                holder = adapter.createViewHolder(container, itemViewType);
            }

            adapter.onBindViewHolder(holder as T, realPosition)
            container.addView(holder.itemView, ViewPager.LayoutParams())

            return holder.itemView
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = adapter.itemCount + 2

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is RecyclerView.ViewHolder) {
                container.removeView(`object`.itemView)
                pool.putRecycledView(`object`)
            }
        }

        fun getRealCount(): Int {
            return adapter.itemCount
        }
    }
}
package io.github.keep2iron.looplayout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.github.keep2iron.fast4android.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.fast4android.base.util.WeakHandler
import io.github.keep2iron.peach.DrawableCreator


/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/08 13:53
 *
 * 可以无限轮播的布局，里面包含了一个ViewPager和一个LinearLayout作为指示器
 */
class FastLoopLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
  : FrameLayout(context, attrs, defStyleAttr) {
  private lateinit var indicators: LinearLayout
  private val weakHandler = WeakHandler()
  private lateinit var adapter: LooperWrapperPagerAdapter<out RecyclerView.ViewHolder>
  val viewPager: ViewPager2 = ViewPager2(context)

  private val runnable = object : Runnable {
    override fun run() {
      viewPager.currentItem = ++currentPosition
      weakHandler.postDelayed(this, playTimeInterval)
    }
  }

  /**
   * 自动轮播时的时间间隔
   */
  var playTimeInterval = 4000L

  /**
   * 非选中时的资源id
   */
  var indicatorUnSelectDrawable: Drawable? = null

  /**
   * 选中时的资源id
   */
  var indicatorSelectDrawable: Drawable? = null

  /**
   * indicator 宽度
   */
  var indicatorWidth: Int = dp2px(context, 8)

  /**
   * indicator 高度
   */
  var indicatorHeight: Int = dp2px(context, 8)

  /**
   * indicator 间距
   */
  var indicatorSpacing: Int = dp2px(context, 10)
  private var flNoDataContainer: FrameLayout

  var indicatorLayoutId: Int = -1

  var onPageChangedListener: ViewPager2.OnPageChangeCallback? = null

  companion object {
    const val DEFAULT_POSITION: Int = 2
  }

  /**
   * 当前选中position
   */
  var currentPosition: Int = DEFAULT_POSITION

  init {
//    LayoutInflater.from(context).inflate(R.layout.fast_widget_loop_layout, this, true)
    //    viewPager.setScroll(true)
//    viewPager.pageMargin = dp2px(context, 12)
//    viewPager.setPaddingLeft(dp2px(context, 24))
//    viewPager.setPaddingRight(dp2px(context, 24))
//    viewPager.setBackgroundColor(Color.RED)
    addView(viewPager, generateDefaultLayoutParams())

    flNoDataContainer = FrameLayout(context)
    val array = resources.obtainAttributes(attrs, R.styleable.FastLoopLayout)
    indicatorSelectDrawable = array.getDrawable(R.styleable.FastLoopLayout_fast_indicator_drawable_select)
    indicatorUnSelectDrawable = array.getDrawable(R.styleable.FastLoopLayout_fast_indicator_drawable_unSelect)
    indicatorWidth = array.getDimensionPixelSize(R.styleable.FastLoopLayout_fast_indicator_drawable_width, indicatorWidth)
    indicatorHeight = array.getDimensionPixelSize(R.styleable.FastLoopLayout_fast_indicator_drawable_height, indicatorHeight)
    indicatorSpacing = array.getDimensionPixelSize(R.styleable.FastLoopLayout_fast_indicator_drawable_spacing, indicatorSpacing)
    indicatorLayoutId = array.getResourceId(R.styleable.FastLoopLayout_fast_indicator_id, indicatorLayoutId)

    val indicatorSelectTintColor = array.getColor(R.styleable.FastLoopLayout_fast_indicator_drawable_select_tint, Color.parseColor("#333333"))
    val indicatorUnSelectTintColor = array.getColor(R.styleable.FastLoopLayout_fast_indicator_drawable_unSelect_tint, Color.parseColor("#7f333333"))

    indicatorSelectDrawable = if (indicatorSelectDrawable == null) {
      DrawableCreator()
        .oval()
        .complete()
        .solidColor(indicatorSelectTintColor)
        .complete()
        .build()
    } else {
      val wrap = DrawableCompat.wrap(indicatorSelectDrawable!!)
      DrawableCompat.setTint(wrap, indicatorSelectTintColor)
      wrap
    }

    indicatorUnSelectDrawable = if (indicatorUnSelectDrawable == null) {
      DrawableCreator()
        .oval()
        .complete()
        .solidColor(indicatorUnSelectTintColor)
        .complete()
        .build()
    } else {
      val wrap = DrawableCompat.wrap(indicatorUnSelectDrawable!!)
      DrawableCompat.setTint(wrap, indicatorUnSelectTintColor)
      wrap
    }

    array.recycle()

    initOnPageChangedListener()
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        weakHandler.removeCallbacks(runnable)
      }
      MotionEvent.ACTION_UP -> {
        if (visibility == View.VISIBLE && viewPager.visibility == View.VISIBLE) {
          weakHandler.postDelayed(runnable, playTimeInterval)
        }
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  private fun initOnPageChangedListener() {
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageScrollStateChanged(state: Int) {
        onPageChangedListener?.onPageScrollStateChanged(state)
        when (state) {
          ViewPager2.SCROLL_STATE_IDLE -> {
            if (currentPosition <= 1) {//No operation
              Log.d(FastLoopLayout::class.java.simpleName, "currentPosition : $currentPosition adapter.getRealCount() + 1 : ${adapter.getRealCount() + 1}")
              viewPager.setCurrentItem(adapter.getRealCount() + 1, false)
            } else if (currentPosition >= adapter.getRealCount() + adapter.spaceItemCount) {
              Log.d(FastLoopLayout::class.java.simpleName, "currentPosition : $currentPosition adapter.spaceItemCount : ${adapter.spaceItemCount}")
              viewPager.setCurrentItem(adapter.spaceItemCount, false)
            }
          }
          ViewPager2.SCROLL_STATE_DRAGGING -> {
            if (currentPosition <= 1) {//No operation
              Log.d(FastLoopLayout::class.java.simpleName, "currentPosition : $currentPosition adapter.getRealCount() + 1 : ${adapter.getRealCount() + 1}")
              viewPager.setCurrentItem(adapter.getRealCount() + 1, false)
            } else if (currentPosition >= adapter.getRealCount() + adapter.spaceItemCount) {
              Log.d(FastLoopLayout::class.java.simpleName, "currentPosition : $currentPosition adapter.spaceItemCount : ${adapter.spaceItemCount}")
              viewPager.setCurrentItem(adapter.spaceItemCount, false)
            }
          }
        }
      }

      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        onPageChangedListener?.onPageScrolled(adapter.toRealPosition(position), positionOffset, positionOffsetPixels)
      }

      override fun onPageSelected(position: Int) {
        if (adapter.getRealCount() == 0) {
          return
        }

        val realPosition = adapter.toRealPosition(position)

        for (i in 0 until indicators.childCount) {
          if (i == realPosition) {
            (indicators.getChildAt(i) as ImageView).setImageDrawable(indicatorSelectDrawable)
          } else {
            (indicators.getChildAt(i) as ImageView).setImageDrawable(indicatorUnSelectDrawable)
          }
        }

        currentPosition = position
        onPageChangedListener?.onPageSelected(realPosition)
      }
    })
  }

  fun setOnEmptyLayoutResId(@LayoutRes layoutRes: Int) {
    LayoutInflater.from(context).inflate(layoutRes, flNoDataContainer, true)
  }

  /**
   * 创建指示器
   */
  fun createIndicator() {
    Log.d(FastLoopLayout::class.java.simpleName, "createIndicator")

    indicators.removeAllViews()

    for (i in 0 until adapter.getRealCount()) {
      val imageView = ImageView(context)
      imageView.apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        val linearParams = LinearLayout.LayoutParams(indicatorWidth, indicatorHeight)
        linearParams.leftMargin = indicatorSpacing
        linearParams.rightMargin = indicatorSpacing
        layoutParams = linearParams
        val realCurrentPosition = adapter.toRealPosition(currentPosition)
        if (i == realCurrentPosition) {
          setImageDrawable(indicatorSelectDrawable)
        } else {
          setImageDrawable(indicatorUnSelectDrawable)
        }
        indicators.addView(imageView)
      }
    }
  }


//  fun toRealPosition(position: Int): Int {
//    var realPosition = (position - adapter.spaceItemCount) % adapter.getRealCount()
//    if (realPosition < 0)
//      realPosition += (adapter.getRealCount() + adapter.spaceItemCount)
//    return realPosition
//  }

  /**
   * 设置RecyclerView的Adapter，主要是因为这个adapter可以和RecyclerView进行结合，共用一个复用池
   *
   * @param recycleAdapter RecyclerView的Adapter
   * @param pool 使用
   */
  fun <T : RecyclerView.ViewHolder> setAdapter(recycleAdapter: RecyclerView.Adapter<T>) {
    recycleAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onChanged() {
        currentPosition = DEFAULT_POSITION
        this@FastLoopLayout.adapter.notifyDataSetChanged()
        createIndicator()
        viewPager.setCurrentItem(currentPosition, false)
        flNoDataContainer.visibility = if (recycleAdapter.itemCount == 0) View.VISIBLE else View.GONE
      }

      override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onItemRangeChanged(positionStart, itemCount, null)
      }

      override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        // fallback to onItemRangeChanged(positionStart, itemCount) if app
        // does not override this method.
        currentPosition = DEFAULT_POSITION
        this@FastLoopLayout.adapter.notifyDataSetChanged()
        createIndicator()
        viewPager.setCurrentItem(currentPosition, false)
        flNoDataContainer.visibility = if (recycleAdapter.itemCount == 0) View.VISIBLE else View.GONE
      }

      override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        currentPosition = DEFAULT_POSITION
        this@FastLoopLayout.adapter.notifyDataSetChanged()
        createIndicator()
        viewPager.setCurrentItem(currentPosition, false)
        flNoDataContainer.visibility = if (recycleAdapter.itemCount == 0) View.VISIBLE else View.GONE
      }

      override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        currentPosition = DEFAULT_POSITION
        this@FastLoopLayout.adapter.notifyDataSetChanged()
        createIndicator()
        viewPager.setCurrentItem(currentPosition, false)
        flNoDataContainer.visibility = if (recycleAdapter.itemCount == 0) View.VISIBLE else View.GONE
      }

      override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        currentPosition = DEFAULT_POSITION
        this@FastLoopLayout.adapter.notifyDataSetChanged()
        createIndicator()
        viewPager.setCurrentItem(currentPosition, false)
        flNoDataContainer.visibility = if (recycleAdapter.itemCount == 0) View.VISIBLE else View.GONE
      }
    })
    this.adapter = LooperWrapperPagerAdapter(recycleAdapter)
    createIndicator()
    viewPager.adapter = adapter

    viewPager.setCurrentItem(DEFAULT_POSITION, false)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    indicators = if (indicatorLayoutId == -1) {
      LinearLayout(context)
    } else {
      findViewById(indicatorLayoutId)
    }

    if (indicators.parent == null) {
      addView(indicators, LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
      ).apply {
        gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        bottomMargin = dp2px(context, 12)
      })
    }
    Log.d(javaClass.simpleName, "onFinishInflate.")
  }

  fun getRecyclerViewAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
    return adapter.recyclerAdapter
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (changedView == this || changedView == viewPager) {
      if (visibility == View.VISIBLE) {
        weakHandler.postDelayed(runnable, playTimeInterval)
      } else {
        weakHandler.removeCallbacks(runnable)
      }
    }
  }

}
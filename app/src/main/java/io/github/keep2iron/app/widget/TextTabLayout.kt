package io.github.keep2iron.app.widget

import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/23 18:05
 */
class TextTabLayout : LinearLayout, ViewPager.OnPageChangeListener {
    enum class Mode {
        /**
         * 微博样式
         */
        WEI_BO,
        /**
         * 陌陌样式
         */
        MO_MO
    }

    private val textViews: ArrayList<TextView> = ArrayList()

    private var viewPager: ViewPager? = null
    /*内部变量*/
    private var currentPosition: Int = 0
    private var scrollPosition: Int = 0
    private var positionOffset: Float = 0.0f
    /**
     * 底部的矩形
     */
    private var rect = RectF()
    /**
     * 是否向右边
     */
    private var isTurnRight = true
    /*外部属性*/

    private var lineLength = 0

    private var rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lineHeight = 0

    private var mode: Mode = Mode.MO_MO

    private var scrollState = ViewPager.SCROLL_STATE_IDLE

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        rectPaint.style = Paint.Style.FILL_AND_STROKE

        val array = resources.obtainAttributes(attrs, R.styleable.TextTabLayout)
        rectPaint.color = array.getColor(R.styleable.TextTabLayout_ttl_line_color, Color.BLACK)
        lineLength = array.getDimensionPixelSize(R.styleable.TextTabLayout_ttl_line_length,
                (5f * context.resources.displayMetrics.density).toInt())
        array.recycle()

        lineHeight = (context.resources.displayMetrics.density * 4.5).toInt()
    }

    fun setTexts(content: ArrayList<String>) {
        val bound = Rect()
        content.forEachIndexed { index, it ->
            val textView = TextView(context)
            if (this.currentPosition == index) {
                textView.textSize = 24f
            } else {
                textView.textSize = 16f
            }
            textView.text = it
            textView.setTextColor(Color.parseColor("#333333"))
            textView.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            textView.setPadding(0, 0, 0, lineHeight + 10)

            val paint = Paint()
            paint.textSize = context.resources.displayMetrics.scaledDensity * 24
            paint.getTextBounds(it, 0, it.length, bound)
            val layoutParams = LinearLayout.LayoutParams(((bound.width() + 20)).toInt(), LayoutParams.MATCH_PARENT)
            textView.layoutParams = layoutParams
            addView(textView)
            textViews.add(textView)
        }
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
        this.scrollState = state
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (currentPosition != position) {
            currentPosition = position
        }
        scrollPosition = position
        this.positionOffset = positionOffset
        isTurnRight = (scrollPosition + positionOffset) - currentPosition > 0

        if (isTurnRight) {
            val maxSize = 24
            val minSize = 16

            val offset = (maxSize - minSize) * positionOffset
            (getChildAt(position) as TextView).textSize = maxSize - offset
            (getChildAt(position + 1) as TextView).textSize = minSize + offset
        }

        invalidate()
    }

    override fun onPageSelected(position: Int) {
        if (scrollState == ViewPager.SCROLL_STATE_SETTLING) {
            textViews.forEach {
                it.paint.isFakeBoldText = false
            }
            textViews[position].paint.isFakeBoldText = true
        } else if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            textViews.forEach {
                it.paint.isFakeBoldText = false
                it.textSize = 16f
            }
            textViews[position].paint.isFakeBoldText = true
            textViews[position].textSize = 24f
        }
        Log.d("tag", "scrollState : $scrollState")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rect.top = height.toFloat() - lineHeight - paddingBottom
        rect.bottom = height.toFloat() - paddingBottom

        for (i in 0 until textViews.size) {
            textViews[i].setOnClickListener {
                viewPager?.currentItem = i
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (textViews.size == 0) {
            Log.w("keep2iron", "please call setTabTextView() to set textViews array")
            return
        }

        when (mode) {
            Mode.WEI_BO -> {
                onDrawWeiBo()
            }
            Mode.MO_MO -> {
                onDrawMoMo()
            }
        }

        canvas.drawRoundRect(rect,
                resources.displayMetrics.density * 5,
                resources.displayMetrics.density * 5,
                rectPaint)

    }

    private fun onDrawWeiBo() {
        if (positionOffset > 0f && positionOffset <= 0.5f && isTurnRight) {
            //向右边移动，移动一半以内
            rect.left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2
            val nextPosRight = textViews[currentPosition + 1].right - (textViews[currentPosition + 1].width - lineLength) * 1f / 2
            rect.right = rect.left + lineLength + (nextPosRight - rect.left - lineLength) * (positionOffset / 0.5f)

        } else if (positionOffset > 0.5f && isTurnRight) {
            //向右边移动，移动一半以上
            val left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2
            val nextLeft = textViews[currentPosition + 1].left + (textViews[currentPosition + 1].width - lineLength) * 1f / 2
            rect.left = left + (nextLeft - left) * (positionOffset - 0.5f) / 0.5f
            rect.right = textViews[currentPosition + 1].right - (textViews[currentPosition + 1].width - lineLength) * 1f / 2

        } else if (positionOffset > 0.5f && !isTurnRight) {
            //向左边移动,移动一半以上
            val nextLeft = textViews[currentPosition - 1].left + (textViews[currentPosition - 1].width - lineLength) * 1f / 2
            val left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2

            rect.left = nextLeft + (left - nextLeft) * (positionOffset - 0.5f) / 0.5f
            rect.right = textViews[currentPosition].right - (textViews[currentPosition].width - lineLength) * 1f / 2
        } else if (positionOffset > 0 && positionOffset <= 0.5f && !isTurnRight) {
            //向左移动。移动一半以下
            val right = textViews[currentPosition].right - (textViews[currentPosition].width - lineLength) * 1f / 2
            val nextRight = textViews[currentPosition - 1].right - (textViews[currentPosition - 1].width - lineLength) * 1f / 2

            rect.left = textViews[currentPosition - 1].left + (textViews[currentPosition - 1].width - lineLength) * 1f / 2
            rect.right = nextRight + (right - nextRight) * (positionOffset / 0.5f)

        } else if (positionOffset <= 0f) {
            //当不移动的时候
            rect.left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2
            rect.right = rect.left + lineLength
        }
    }

    private fun onDrawMoMo() {
        if (isTurnRight) {
            val left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2
            val nextLeft = textViews[currentPosition + 1].left + (textViews[currentPosition + 1].width - lineLength) * 1f / 2

            rect.left = left + (nextLeft - left) * positionOffset

            if (positionOffset > 0f && positionOffset <= 0.5f) {
                rect.right = rect.left + lineLength + (lineLength) * (positionOffset / 0.5f)
            } else {
                rect.right = rect.left + lineLength + (lineLength) * ((1 - positionOffset) / 0.5f)
            }
        } else if (!isTurnRight || positionOffset <= 0f) {
            rect.left = textViews[currentPosition].left + (textViews[currentPosition].width - lineLength) * 1f / 2
            rect.right = rect.left + lineLength
        }
    }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
        invalidate()
    }
}
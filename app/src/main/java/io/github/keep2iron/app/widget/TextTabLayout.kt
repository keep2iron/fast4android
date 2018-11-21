package io.github.keep2iron.app.widget

import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/23 18:05
 */
class TextTabLayout : FrameLayout, ViewPager.OnPageChangeListener {
    private val textViews: ArrayList<TextView> = ArrayList()
    private val texts: ArrayList<String> = ArrayList()

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

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        rectPaint.style = Paint.Style.FILL_AND_STROKE

        val array = resources.obtainAttributes(attrs, R.styleable.TextTabLayout)
        rectPaint.color = array.getColor(R.styleable.TextTabLayout_ttl_line_color, Color.BLACK)
        lineLength = array.getDimensionPixelSize(R.styleable.TextTabLayout_ttl_line_length,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
        array.recycle()

        texts.forEach {
//            val textView = TextView(context)
//            textView.textSize =
//                    addView(textView)
        }
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (currentPosition != position) {
            currentPosition = position
        }
        scrollPosition = position
        this.positionOffset = positionOffset
        isTurnRight = (scrollPosition + positionOffset) - currentPosition > 0

        invalidate()
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rect.top = 0f
        rect.bottom = height.toFloat()

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
        canvas.drawRoundRect(rect,
                resources.displayMetrics.density * 3,
                resources.displayMetrics.density * 3,
                rectPaint)
    }

    fun setTabTextView(vararg text: String) {
        this.texts.addAll(text)

    }

    fun setTabTextViewArray(textArr: Array<String>) {
        this.texts.addAll(textArr)
    }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
        invalidate()
    }
}
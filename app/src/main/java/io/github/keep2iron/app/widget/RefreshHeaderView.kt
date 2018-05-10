package io.github.keep2iron.app.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/04 17:58
 *
 * https://dribbble.com/shots/2590603-Liquid-Pull-Down-UI-Animation
 */
class RefreshHeaderView : View, RefreshHeader {

    private val backgroundCircles: ArrayList<Circle> = ArrayList()
    private val background2Circles: ArrayList<Circle> = ArrayList()

    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val background2Paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progress: Float = 0.0f
    private var backgroundColorInt: Int = 0

    private var animator: ValueAnimator? = null
    private var isLayout: Boolean = false

    class Circle {
        private var originR: Float = -1f

        var x: Float = 0f
        var y: Float = 0f
        var r: Float = 0f
            set(value) {
                if (originR < 0) {
                    originR = value
                }
                field = value
            }
        var v: Float = 0f

        fun restoreY() {
            r = originR
            y = (Circle.originY + originR + Math.random() * originR).toFloat()
        }

        companion object {
            var originY: Float = 0f
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()

        val array = resources.obtainAttributes(attrs, R.styleable.RefreshHeaderView)
        backgroundColorInt = array.getColor(R.styleable.RefreshHeaderView_rhv_background, Color.WHITE)
        array.recycle()

        setBackgroundColor(backgroundColorInt)
    }

    /**
     * init view
     */
    private fun initView() {
        backgroundPaint.color = Color.WHITE
        background2Paint.color = Color.parseColor("#ffb5d3")
    }

    private fun initBackgroundCircle(circles: ArrayList<Circle>) {
        val step = 40
        val maxRadius = step * 2f
        val minRadius = step * 1.2f
        val maxSpeed = 5f
        val minSpeed = 1.3f
        var i = 0
        while (i <= resources.displayMetrics.widthPixels) {
            for (j in 0..(Math.random() * 2 + 1).toInt()) {
                val circle = Circle()
                circle.r = (Math.random() * (maxRadius - minRadius) + minRadius).toFloat()
                circle.v = (Math.random() * (maxSpeed - minSpeed) + minSpeed).toFloat()
                circle.x = (i + Math.random() * step).toFloat()

                circles.add(circle)
            }
            i += step
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isLayout) {
            isLayout = true
            backgroundCircles.forEach { circle ->
                circle.y = (Circle.originY - circle.r + Math.random() * 2 * circle.r).toFloat()
            }
            background2Circles.forEach { circle ->
                circle.y = (Circle.originY - circle.r + Math.random() * 2 * circle.r).toFloat()
            }
        }
        if (changed) {
            Circle.originY = height.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawCircle(background2Circles, background2Paint, canvas)
        drawCircle(backgroundCircles, backgroundPaint, canvas)
    }

    private fun drawCircle(circles: ArrayList<Circle>, paint: Paint, canvas: Canvas) {
        circles.forEach { circle ->
            if (circle.y - circle.v < 0 || circle.r <= 0) {
                circle.restoreY()
            } else {
                canvas.drawCircle(circle.x, circle.y, circle.r, paint)
                circle.y -= circle.v
                if (circle.y < height - circle.r * 1.1f) {
                    circle.r -= 1.5f
                }
            }
        }
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        postDelayed({
            animator?.pause()
        }, 500)
        return 500
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, extendHeight: Int) {
        kernel.requestDrawBackgoundForHeader(backgroundColorInt)
        initBackgroundCircle(backgroundCircles)
        initBackgroundCircle(background2Circles)
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun getView(): View = this

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onReleasing(percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
    }

    override fun onPullingDown(percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                duration = 3000
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener { anim ->
                    progress = anim.animatedValue as Float
                    isLayout = true
                    invalidate()
                }
            }
        }
        animator?.start()
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, extendHeight: Int) {
    }

    override fun onStateChanged(refreshLayout: RefreshLayout?, oldState: RefreshState, newState: RefreshState) {
    }

    override fun isSupportHorizontalDrag(): Boolean = false
}
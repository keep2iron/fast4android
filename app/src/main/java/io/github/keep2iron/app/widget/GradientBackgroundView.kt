package io.github.keep2iron.app.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import io.github.keep2iron.app.R

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/03/29 10:26
 *
 * 用于切换Tab时颜色进行环形渐变的效果
 */
class GradientBackgroundView : View {
    /**
     * 当前的颜色
     */
    private var currentColor: Int = 0
    private var duration: Long = 300

    private var nextColor: Int = 0
    private var progress: Float = 0.0f
    private var weight: Float = 0.5f
    private var radius: Float = 0f
    private var animator: ValueAnimator? = null
    private val paint: Paint = Paint()
    private var listener: (() -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        currentColor = ContextCompat.getColor(context, R.color.white)
        val array = resources.obtainAttributes(attrs, R.styleable.GradientBackgroundView)
        paint.color = currentColor
        paint.isAntiAlias = true

        attrs?.let {
            currentColor = array.getColor(R.styleable.GradientBackgroundView_gbv_default_color, ContextCompat.getColor(context, R.color.white))
            duration = array.getInteger(R.styleable.GradientBackgroundView_gbv_duration, duration.toInt()).toLong()
        }

        array.recycle()
    }

    fun setDefaultColor(@ColorRes color: Int) {
        currentColor = ContextCompat.getColor(context, color)
    }


    fun setOnAnimatorFinishListener(listener: () -> Unit) {
        this.listener = listener
    }


    /**
     * 进行准备下一个颜色的动画
     * @param weight 起始于水平方向的权重 0.0f - 1.0f
     * @param color 下一个颜色
     */
    fun animatorNextColor(weight: Float, @ColorRes color: Int) {
        if (animator != null) {
            animator?.cancel()
            progress = 1.0f
            listener?.invoke()
            invalidate()
        }

        nextColor = ContextCompat.getColor(context, color)
        paint.color = nextColor

        this.weight = weight
        val maxWeight = if (weight > 0.5f) weight else 1f - weight
        this.radius = Math.sqrt(Math.pow((maxWeight * measuredWidth).toDouble(), 2.0)
                + Math.pow(measuredHeight.toDouble(), 2.0)).toFloat()

        animator = ObjectAnimator.ofFloat(0f, 1.0f)
        animator?.apply {
            duration = this@GradientBackgroundView.duration
            interpolator = LinearInterpolator()
            start()
        }?.addUpdateListener { anim ->
            this@GradientBackgroundView.progress = anim.animatedValue as Float
            this@GradientBackgroundView.invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (progress >= 1.0f) {
            currentColor = nextColor
            progress = 0.0f
            listener?.invoke()
            canvas.drawColor(currentColor)
        } else if (progress < 1.0f && progress > 0.0f) {
            canvas.drawColor(currentColor)
            canvas.save()
            canvas.clipRect(0, 0, width, height)
            canvas.drawCircle(weight * width, height.toFloat(), radius * progress, paint)
            canvas.restore()
        } else {
            canvas.drawColor(currentColor)
        }
    }
}
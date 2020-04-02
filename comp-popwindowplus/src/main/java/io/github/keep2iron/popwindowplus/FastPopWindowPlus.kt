package io.github.keep2iron.popwindowplus

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2017/11/21 16:39
 *
 * 下拉菜单显示控件，解决了PopWindow表现力不足的问题，动画效果为后面的阴影渐变，上面的container再进行下拉动画的显示，点击背景的阴影层，收起container
 */
class FastPopWindowPlus(context: Context) : FrameLayout(context) {

  private lateinit var contentView: View
  var isShowing: Boolean = false
  var isAnim: Boolean = false
  val backgroundView: View
  var translateView: View? = null

  private var cancelable = true

  private var onDismissListener: (() -> Unit)? = null

  init {
    LayoutInflater.from(context).inflate(R.layout.comp_widget_slide_drop, this, true)
    backgroundView = findViewById(R.id.background)
  }

  fun setContentView(view: View) {
    contentView = view

    if (contentView.layoutParams == null) {
      contentView.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    addView(contentView, contentView.layoutParams)
  }

  private fun getYAtScreen(view: View, top: Int): Int {
    return if (view.parent == null || view.parent === view.rootView) {
      top
    } else getYAtScreen(view.parent as View, top + view.top)

  }

  fun showViewAsBelow(view: View) {
    if (isAnim || isShowing) {
      return
    }
    isShowing = true

    val rootView = view.rootView as ViewGroup

    val viewTop = getYAtScreen(view, 0)
    val y = viewTop + view.height

    if (this.translateView != null) {
      rootView.removeView(this.translateView)
    }
    val translateView = View(context)
    translateView.layoutParams = LayoutParams(MATCH_PARENT, viewTop)
    rootView.addView(translateView)
    this.translateView = translateView

    val params = LayoutParams(MATCH_PARENT, rootView.height - y)
    params.setMargins(0, y, 0, 0)
    layoutParams = params
    rootView.addView(this@FastPopWindowPlus)

    if (cancelable) {
      val listener = OnClickListener {
        if (!isAnim) {
          dismiss()
        }
      }
      backgroundView.setOnClickListener(listener)
      translateView.setOnClickListener(listener)
    }

    startAnim(null)
  }

  fun dismiss() {
    if (isAnim || !isShowing) {
      return
    }

    isAnim = true
    isShowing = false
    startAnim(object : Animation.AnimationListener {
      override fun onAnimationRepeat(animation: Animation?) {}

      override fun onAnimationEnd(animation: Animation?) {
        val rootView = rootView as ViewGroup
        rootView.removeView(this@FastPopWindowPlus)
        if (this@FastPopWindowPlus.translateView != null) {
          rootView.removeView(this@FastPopWindowPlus.translateView)
        }

        isAnim = false
        onDismissListener?.invoke()
      }

      override fun onAnimationStart(animation: Animation?) {}
    })
  }

  private fun startAnim(listener: Animation.AnimationListener?) {
    val startAlpha: Float
    val endAlpha: Float

    val startTrans: Float
    val endTrans: Float

    if (isShowing) {
      startAlpha = 0.0f
      endAlpha = 1.0f
      startTrans = -1.0f
      endTrans = 0.0f
    } else {
      startAlpha = 1.0f
      endAlpha = 0.0f
      startTrans = 0.0f
      endTrans = -1.0f
    }

    val objectAnimator = ObjectAnimator.ofFloat(backgroundView, "alpha", startAlpha, endAlpha)
      .setDuration(200)
    objectAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        listener?.onAnimationEnd(null)
      }

      override fun onAnimationStart(animation: Animator) {
        listener?.onAnimationStart(null)

        val translateAnimation = TranslateAnimation(
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, startTrans,
          Animation.RELATIVE_TO_SELF, endTrans
        )
        translateAnimation.fillAfter = true
        translateAnimation.interpolator = LinearInterpolator()
        translateAnimation.duration = 200
        contentView.startAnimation(translateAnimation)
      }
    })
    objectAnimator.start()
  }

  fun setOnDismissListener(onDismissListener: () -> Unit) {
    this.onDismissListener = onDismissListener
  }

  fun setCancelable(cancelable: Boolean) {
    this.cancelable = cancelable
  }
}
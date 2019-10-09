/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.keep2iron.base.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.ViewStub
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger

private val APPCOMPAT_CHECK_ATTRS = intArrayOf(androidx.appcompat.R.attr.colorPrimary)

/**
 * 判断是否需要对 LineSpacingExtra 进行额外的兼容处理
 * 安卓 5.0 以下版本中，LineSpacingExtra 在最后一行也会产生作用，因此会多出一个 LineSpacingExtra 的空白，可以通过该方法判断后进行兼容处理
 * if (ViewExt.getISLastLineSpacingExtraError()) {
 * textView.bottomMargin = -3dp;
 * } else {
 * textView.bottomMargin = 0;
 * }
 */
fun isLastLineSpacingExtraError() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

fun View.checkAppCompatTheme() {
    val a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS)
    val failed = !a.hasValue(0)
    a.recycle()
    require(!failed) { "You need to use a Theme.AppCompat theme " + "(or descendant) with the design library." }
}

fun Activity.rootView(): View =
        (findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)

fun Window.requestCompatApplyInsets() {
    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        decorView.requestFitSystemWindows()
    } else if (Build.VERSION.SDK_INT >= 21) {
        decorView.requestApplyInsets()
    }
}

fun View.setCompatBackground(drawable: Drawable?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        setBackgroundDrawable(drawable)
    }
}

fun View.setBackgroundKeepingPadding(drawable: Drawable?) {
    val padding =
            intArrayOf(paddingLeft, paddingTop, paddingRight, paddingBottom)
    setCompatBackground(drawable)
    setPadding(padding[0], padding[1], padding[2], padding[3])
}

fun View.setBackgroundColorKeepPadding(@ColorInt color: Int) {
    val padding =
            intArrayOf(paddingLeft, paddingTop, paddingRight, paddingBottom)
    setBackgroundColor(color)
    setPadding(padding[0], padding[1], padding[2], padding[3])
}

/**
 * 对 View 的做背景闪动的动画
 */
fun View.setBackgroundKeepingPadding(backgroundResId: Int) {
    setBackgroundKeepingPadding(ContextCompat.getDrawable(context, backgroundResId))
}

/**
 * 对 View 的做背景闪动的动画
 */
fun View.playBackgroundBlinkAnimation(@ColorInt bgColor: Int) {
    val alphaArray = intArrayOf(0, 255, 0)
    playViewBackgroundAnimation(bgColor, alphaArray, 300)
}

fun View.playViewBackgroundAnimation(
        @ColorInt bgColor: Int,
        alphaArray: IntArray,
        stepDuration: Int
) {
    playViewBackgroundAnimation(bgColor, alphaArray, stepDuration, null)
}

/**
 * 对 View 做背景色变化的动作
 *
 * @param v            做背景色变化的View
 * @param bgColor      背景色
 * @param alphaArray   背景色变化的alpha数组，如 int[]{255,0} 表示从纯色变化到透明
 * @param stepDuration 每一步变化的时长
 * @param endAction    动画结束后的回调
 */
fun View.playViewBackgroundAnimation(
        @ColorInt bgColor: Int,
        alphaArray: IntArray,
        stepDuration: Int,
        endAction: Runnable?
): Animator {
    val animationCount = alphaArray.size - 1

    val bgDrawable = ColorDrawable(bgColor)
    val oldBgDrawable = background
    setBackgroundKeepingPadding(bgDrawable)

    val animatorList = ArrayList<Animator>()
    for (i in 0 until animationCount) {
        val animator = ObjectAnimator.ofInt(background, "alpha", alphaArray[i], alphaArray[i + 1])
        animatorList.add(animator)
    }

    val animatorSet = AnimatorSet()
    animatorSet.duration = stepDuration.toLong()
    animatorSet.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            setBackgroundKeepingPadding(oldBgDrawable)
            endAction?.run()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    })
    animatorSet.playSequentially(animatorList)
    animatorSet.start()
    return animatorSet
}

/**
 *
 * 对 View 做透明度变化的进场动画。
 *
 * 相关方法 [.fadeOut]
 *
 * @param view            做动画的 View
 * @param duration        动画时长(毫秒)
 * @param listener        动画回调
 * @param isNeedAnimation 是否需要动画
 */
fun View.fadeIn(
        duration: Int,
        listener: Animation.AnimationListener? = null,
        isNeedAnimation: Boolean = true
): AlphaAnimation? {
    if (isNeedAnimation) {
        visibility = View.VISIBLE
        val alpha = AlphaAnimation(0f, 1f)
        alpha.interpolator = DecelerateInterpolator()
        alpha.duration = duration.toLong()
        alpha.fillAfter = true
        if (listener != null) {
            alpha.setAnimationListener(listener)
        }
        startAnimation(alpha)
        return alpha
    } else {
        alpha = 1f
        visibility = View.VISIBLE
        return null
    }
}

/**
 *
 * 对 View 做透明度变化的退场动画
 *
 * 相关方法 [.fadeIn]
 *
 * @param view            做动画的 View
 * @param duration        动画时长(毫秒)
 * @param listener        动画回调
 * @param isNeedAnimation 是否需要动画
 */
fun View.fadeOut(
        duration: Int,
        listener: Animation.AnimationListener? = null,
        isNeedAnimation: Boolean = true
): AlphaAnimation? {
    if (isNeedAnimation) {
        val alpha = AlphaAnimation(1f, 0f)
        alpha.interpolator = DecelerateInterpolator()
        alpha.duration = duration.toLong()
        alpha.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                listener?.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animation) {
                visibility = View.GONE
                listener?.onAnimationEnd(animation)
            }

            override fun onAnimationRepeat(animation: Animation) {
                listener?.onAnimationRepeat(animation)
            }
        })
        startAnimation(alpha)
        return alpha
    } else {
        visibility = View.GONE
        return null
    }
}

/**
 *
 * 对 View 做上下位移的退场动画
 *
 * 相关方法 [.slideIn]
 *
 * @param view            做动画的 View
 * @param duration        动画时长(毫秒)
 * @param listener        动画回调
 * @param isNeedAnimation 是否需要动画
 * @param direction       进场动画的方向
 * @return 动画对应的 Animator 对象, 注意无动画时返回 null
 */
fun View.slideOut(
        duration: Int,
        listener: Animation.AnimationListener? = null,
        isNeedAnimation: Boolean = true,
        direction: FastAnimDirection = FastAnimDirection.RIGHT_TO_LEFT
): TranslateAnimation? {
    if (isNeedAnimation) {
        var translate: TranslateAnimation? = null
        when (direction) {
            FastAnimDirection.LEFT_TO_RIGHT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            )
            FastAnimDirection.TOP_TO_BOTTOM -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
            )
            FastAnimDirection.RIGHT_TO_LEFT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            )
            FastAnimDirection.BOTTOM_TO_TOP -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f
            )
        }
        translate.interpolator = DecelerateInterpolator()
        translate.duration = duration.toLong()
        translate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                listener?.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animation) {
                visibility = View.GONE
                listener?.onAnimationEnd(animation)
            }

            override fun onAnimationRepeat(animation: Animation) {
                listener?.onAnimationRepeat(animation)
            }
        })
        startAnimation(translate)
        return translate
    } else {
        clearAnimation()
        visibility = View.GONE
        return null
    }

}

/**
 *
 * 对 View 做上下位移的进场动画
 *
 * 相关方法 [.slideOut]
 *
 * @param view            做动画的 View
 * @param duration        动画时长(毫秒)
 * @param listener        动画回调
 * @param isNeedAnimation 是否需要动画
 * @param direction       进场动画的方向
 * @return 动画对应的 Animator 对象, 注意无动画时返回 null
 */
fun View.slideIn(
        duration: Int,
        listener: Animation.AnimationListener? = null,
        isNeedAnimation: Boolean = true,
        direction: FastAnimDirection = FastAnimDirection.LEFT_TO_RIGHT
): TranslateAnimation? {
    if (isNeedAnimation) {
        var translate: TranslateAnimation? = null
        when (direction) {
            FastAnimDirection.LEFT_TO_RIGHT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            )
            FastAnimDirection.TOP_TO_BOTTOM -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f
            )
            FastAnimDirection.RIGHT_TO_LEFT -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            )
            FastAnimDirection.BOTTOM_TO_TOP -> translate = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
            )
        }
        translate.interpolator = DecelerateInterpolator()
        translate.duration = duration.toLong()
        translate.fillAfter = true
        if (listener != null) {
            translate.setAnimationListener(listener)
        }
        visibility = View.VISIBLE
        startAnimation(translate)
        return translate
    } else {
        clearAnimation()
        visibility = View.VISIBLE

        return null
    }
}

/**
 * 对 View 设置 paddingLeft
 *
 * @param view  需要被设置的 View
 * @param value 设置的值
 */
fun View.setPaddingLeft(value: Int) {
    if (value != paddingLeft) {
        setPadding(value, paddingTop, paddingRight, paddingBottom)
    }
}

/**
 * 对 View 设置 paddingTop
 *
 * @param view  需要被设置的 View
 * @param value 设置的值
 */
fun View.setPaddingTop(value: Int) {
    if (value != paddingTop) {
        setPadding(paddingLeft, value, paddingRight, paddingBottom)
    }
}

/**
 * 对 View 设置 paddingRight
 *
 * @param view  需要被设置的 View
 * @param value 设置的值
 */
fun View.setPaddingRight(value: Int) {
    if (value != paddingRight) {
        setPadding(paddingLeft, paddingTop, value, paddingBottom)
    }
}

/**
 * 对 View 设置 paddingBottom
 *
 * @param view  需要被设置的 View
 * @param value 设置的值
 */
fun View.setPaddingBottom(value: Int) {
    if (value != paddingBottom) {
        setPadding(paddingLeft, paddingTop, paddingRight, value)
    }
}

/**
 * inflate ViewStub 并返回对应的 View。
 */
fun View.findViewFromViewStub(
        viewStubId: Int,
        inflatedViewId: Int,
        inflateLayoutResId: Int
): View? {
    var view: View? = findViewById(inflatedViewId)
    if (null == view) {
        val vs = findViewById<View>(viewStubId) as ViewStub ?: return null
        if (vs.layoutResource < 1 && inflateLayoutResId > 0) {
            vs.layoutResource = inflateLayoutResId
        }
        view = vs.inflate()
        if (null != view) {
            view = view.findViewById(inflatedViewId)
        }
    }
    return view
}

fun View.clearValueAnimator(animator: Animator) {
    animator.removeAllListeners()
    if (animator is ValueAnimator) {
        animator.removeAllUpdateListeners()
    }

    if (Build.VERSION.SDK_INT >= 19) {
        animator.pause()
    }
    animator.cancel()
}

fun View.calcViewScreenLocation(): Rect {
    val location = IntArray(2)
    // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
    getLocationOnScreen(location)
    return Rect(
            location[0], location[1], location[0] + width,
            location[1] + height
    )
}

/**
 * 扩展点击区域的范围
 *
 * @param view       需要扩展的元素，此元素必需要有父级元素
 * @param expendSize 需要扩展的尺寸（以sp为单位的）
 */
fun View.expendTouchArea(expendSize: Int) {
    val parentView = parent as View?
    val thisView = this

    parentView?.post {
        val rect = Rect()
        getHitRect(rect) //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
        rect.left -= expendSize
        rect.top -= expendSize
        rect.right += expendSize
        rect.bottom += expendSize
        parentView.touchDelegate = TouchDelegate(rect, thisView)
    }
}

object ViewExt {

    // copy from View.generateViewId for API <= 16
    private val sNextGeneratedId = AtomicInteger(1)

    private val APPCOMPAT_CHECK_ATTRS = intArrayOf(androidx.appcompat.R.attr.colorPrimary)

    val isLastLineSpacingExtraError: Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    /**
     * 扩展点击区域的范围
     *
     * @param view       需要扩展的元素，此元素必需要有父级元素
     * @param expendSize 需要扩展的尺寸（以sp为单位的）
     */
    fun expendTouchArea(view: View?, expendSize: Int) {
        if (view != null) {
            val parentView = view.parent as View

            parentView.post {
                val rect = Rect()
                view.getHitRect(rect) //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, view)
            }
        }
    }

//  /**
//   * requestDisallowInterceptTouchEvent 的安全方法。存在它的原因是 QMUIPullRefreshLayout 会拦截这个事件
//   *
//   * @param view
//   * @param value
//   */
//  fun safeRequestDisallowInterceptTouchEvent(view: View, value: Boolean) {
//    val viewParent = view.parent
//    if (viewParent != null) {
//      var layoutInflate = viewParent
//      while (layoutInflate != null) {
//        if (layoutInflate is QMUIPullRefreshLayout) {
//          (layoutInflate as QMUIPullRefreshLayout).openSafeDisallowInterceptTouchEvent()
//        }
//        layoutInflate = layoutInflate.parent
//      }
//      viewParent.requestDisallowInterceptTouchEvent(value)
//    }
//  }

    /**
     * 把 ViewStub inflate 之后在其中根据 id 找 View
     *
     * @param parentView     包含 ViewStub 的 View
     * @param viewStubId     要从哪个 ViewStub 来 inflate
     * @param inflatedViewId 最终要找到的 View 的 id
     * @return id 为 inflatedViewId 的 View
     */
    fun findViewFromViewStub(parentView: View?, viewStubId: Int, inflatedViewId: Int): View? {
        if (null == parentView) {
            return null
        }
        var view: View? = parentView.findViewById(inflatedViewId)
        if (null == view) {
            val vs = parentView.findViewById<View>(viewStubId) as ViewStub ?: return null
            view = vs.inflate()
            if (null != view) {
                view = view.findViewById(inflatedViewId)
            }
        }
        return view
    }

    fun safeSetImageViewSelected(imageView: ImageView, selected: Boolean) {
        // imageView setSelected 实现有问题。
        // resizeFromDrawable 中判断 drawable size 是否改变而调用 requestLayout，看似合理，但不会被调用
        // 因为 super.setSelected(selected) 会调用 refreshDrawableState
        // 而从 android 6 以后， ImageView 会重载refreshDrawableState，并在里面处理了 drawable size 改变的问题,
        // 从而导致 resizeFromDrawable 的判断失效
        val drawable = imageView.drawable ?: return
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        imageView.isSelected = selected
        if (drawable.intrinsicWidth != drawableWidth || drawable.intrinsicHeight != drawableHeight) {
            imageView.requestLayout()
        }
    }

    fun setImageViewTintColor(imageView: ImageView, @ColorInt tintColor: Int): ColorFilter {
        val colorFilter = LightingColorFilter(Color.argb(255, 0, 0, 0), tintColor)
        imageView.colorFilter = colorFilter
        return colorFilter
    }

    /**
     * 判断 ListView 是否已经滚动到底部。
     *
     * @param listView 需要被判断的 ListView。
     * @return ListView 已经滚动到底部则返回 true，否则返回 false。
     */
    fun isListViewAlreadyAtBottom(listView: ListView): Boolean {
        if (listView.adapter == null || listView.height == 0) {
            return false
        }

        if (listView.lastVisiblePosition == listView.adapter.count - 1) {
            val lastItemView = listView.getChildAt(listView.childCount - 1)
            if (lastItemView != null && lastItemView.bottom == listView.height) {
                return true
            }
        }
        return false
    }

    /**
     * Retrieve the transformed bounding rect of an arbitrary descendant view.
     * This does not need to be a direct child.
     *
     * @param descendant descendant view to reference
     * @param out        rect to set to the bounds of the descendant view
     */
    fun getDescendantRect(parent: ViewGroup, descendant: View, out: Rect) {
        out.set(0, 0, descendant.width, descendant.height)
        ViewGroupHelper.offsetDescendantRect(
                parent,
                descendant,
                out
        )
    }

    private object ViewGroupHelper {
        private val sMatrix = ThreadLocal<Matrix>()
        private val sRectF = ThreadLocal<RectF>()

        fun offsetDescendantRect(group: ViewGroup, child: View, rect: Rect) {
            var m = sMatrix.get()
            if (m == null) {
                m = Matrix()
                sMatrix.set(m)
            } else {
                m.reset()
            }

            offsetDescendantMatrix(
                    group,
                    child,
                    m
            )

            var rectF = sRectF.get()
            if (rectF == null) {
                rectF = RectF()
                sRectF.set(rectF)
            }
            rectF.set(rect)
            m.mapRect(rectF)
            rect.set(
                    (rectF.left + 0.5f).toInt(), (rectF.top + 0.5f).toInt(),
                    (rectF.right + 0.5f).toInt(), (rectF.bottom + 0.5f).toInt()
            )
        }

        internal fun offsetDescendantMatrix(target: ViewParent, view: View, m: Matrix) {
            val parent = view.parent
            if (parent is View && parent !== target) {
                val vp = parent as View
                offsetDescendantMatrix(
                        target,
                        vp,
                        m
                )
                m.preTranslate((-vp.scrollX).toFloat(), (-vp.scrollY).toFloat())
            }

            m.preTranslate(view.left.toFloat(), view.top.toFloat())

            if (!view.matrix.isIdentity) {
                m.preConcat(view.matrix)
            }
        }
    }
}

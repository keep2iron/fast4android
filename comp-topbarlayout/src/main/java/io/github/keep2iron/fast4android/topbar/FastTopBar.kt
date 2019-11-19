package io.github.keep2iron.fast4android.topbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import io.github.keep2iron.base.util.FastDisplayHelper
import io.github.keep2iron.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.base.util.FastDisplayHelper.sp2px
import io.github.keep2iron.base.util.getAttrColor
import io.github.keep2iron.base.util.getAttrDimen
import io.github.keep2iron.fast4android.core.alpha.FastDrawableRoundViewHelper
import io.github.keep2iron.fast4android.core.alpha.FastRoundImageButton
import kotlin.LazyThreadSafetyMode.NONE

class FastTopBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.FastTopBarStyle
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_VIEW_ID = -1
    }

    private val fastDrawableViewHelper by lazy(NONE) {
        FastDrawableRoundViewHelper()
    }

    val titleContainer by lazy(NONE) {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(
                linearLayout, LayoutParams(
                LayoutParams.MATCH_PARENT,
                context.getAttrDimen(R.attr.fast_topbar_height)
        )
        )
        linearLayout
    }

    val titleView: TextView by lazy(NONE) {
        TextView(context)
    }

    var title: String? = null
        set(value) {
            field = value

            if (!value.isNullOrEmpty()) {
                titleView.text = value
            }
        }

    var titleGravity = Gravity.CENTER
        set(value) {
            field = value

            titleView.gravity = field
        }

    var titleTextSizeInPixel: Int = sp2px(context, 16)
        set(value) {
            field = value

            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
        }

    var titleColor: Int = context.getAttrColor(R.attr.fast_config_color_gray_1)
        set(value) {
            field = value

            titleView.setTextColor(titleColor)
        }

    val backgroundDrawable by lazy(NONE) {
        if (background != null) {
            val separatorDrawable = ShapeDrawable()
            separatorDrawable.paint.style = Paint.Style.FILL
            separatorDrawable.paint.color = separatorColor

            val layerDrawable = LayerDrawable(arrayOf(separatorDrawable, background))
            layerDrawable.setLayerInset(1, 0, 0, 0, separatorHeight)
            layerDrawable
        } else {
            null
        }
    }

    private var separatorColor: Int = context.getAttrColor(R.attr.fast_config_color_separator)

    private var separatorHeight: Int = dp2px(context, 1)

    private var backDrawableRes: Int = R.drawable.fast_icon_topbar_back

    /**
     * 左边布局中 最右边的view id
     */
    private var leftLastId: Int = DEFAULT_VIEW_ID

    /**
     * 右边布局中 最左边的view id
     */
    private var rightLastId: Int = DEFAULT_VIEW_ID

    private val leftViews = ArrayList<View>()

    private val rightViews = ArrayList<View>()

    init {
        val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.FastTopBar, defStyleAttr, 0)
        if (attrs != null) {
            val drawableCreator = fastDrawableViewHelper.resolveAttribute(context, attrs, defStyleAttr)
            background = drawableCreator?.build() ?: background

            resolveTypedArray(typedArray)
        }

        typedArray.recycle()
    }

    fun resolveTypedArray(typedArray: TypedArray) {
        title = typedArray.getString(R.styleable.FastTopBar_fast_topbar_title)
        //如果title为空跳过则跳过设置
        titleGravity =
                typedArray.getInt(R.styleable.FastTopBar_fast_topbar_title_gravity, titleGravity)
        titleTextSizeInPixel = typedArray.getDimensionPixelSize(
                R.styleable.FastTopBar_fast_topbar_title_text_size,
                titleTextSizeInPixel
        )
        titleColor = typedArray.getColor(
                R.styleable.FastTopBar_fast_topbar_title_color,
                titleColor
        )

        separatorColor = typedArray.getColor(
                R.styleable.FastTopBar_fast_topbar_separator_color,
                separatorColor
        )
        separatorHeight = typedArray.getDimensionPixelSize(
                R.styleable.FastTopBar_fast_topbar_separator_height,
                separatorHeight
        )

        backDrawableRes =
                typedArray.getResourceId(
                        R.styleable.FastTopBar_fast_topbar_left_back_drawable_id,
                        backDrawableRes
                )

        titleContainer.addView(
                titleView, LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                context.getAttrDimen(R.attr.fast_topbar_height)
        ).apply {
            gravity = titleGravity
        }
        )

        background = backgroundDrawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var leftSize = paddingLeft
        if (leftViews.isNotEmpty()) {
            leftSize += leftViews.asSequence()
                    .map {
                        if (it.visibility == View.VISIBLE)
                            it.measuredWidth
                        else
                            0
                    }
                    .reduce { acc, i ->
                        acc + i
                    }
        }

        var rightSize = paddingRight
        if (rightViews.isNotEmpty()) {
            rightSize += rightViews.asSequence()
                    .map {
                        if (it.visibility == View.VISIBLE)
                            it.measuredWidth
                        else
                            0
                    }
                    .reduce { acc, i ->
                        acc + i
                    }
        }
        //找到左右两边大小的最大值
        val maxSize = leftSize.coerceAtLeast(rightSize)
        val titleWidth = MeasureSpec.getSize(widthMeasureSpec) - maxSize * 2

        titleContainer.measure(titleWidth, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        var leftSize = paddingLeft
        if (leftViews.isNotEmpty()) {
            leftSize += leftViews.asSequence()
                    .map {
                        if (it.visibility == View.VISIBLE)
                            it.measuredWidth
                        else
                            0
                    }
                    .reduce { acc, i ->
                        acc + i
                    }
        }

        var rightSize = paddingRight
        if (rightViews.isNotEmpty()) {
            rightSize += rightViews.asSequence()
                    .map {
                        if (it.visibility == View.VISIBLE)
                            it.measuredWidth
                        else
                            0
                    }
                    .reduce { acc, i ->
                        acc + i
                    }
        }
        //找到左右两边大小的最大值
        val maxSize = leftSize.coerceAtLeast(rightSize)

        titleContainer.layout(maxSize, 0, width - maxSize, titleContainer.bottom)
    }

    fun addLeftBackImageButton(
            drawable: Drawable? = ContextCompat.getDrawable(context, backDrawableRes),
            @IdRes backId: Int = R.id.fast_topbar_item_left_back,
            @ColorRes tintColorRes: Int = -1
    ): View {
        val imageView = FastRoundImageButton(context)
        if (tintColorRes != -1) {
            drawable?.setColorFilter(
                    ContextCompat.getColor(context, tintColorRes),
                    PorterDuff.Mode.SRC_ATOP
            )
        } else {
            drawable?.setColorFilter(
                    titleColor,
                    PorterDuff.Mode.SRC_ATOP
            )
        }
        drawable?.setBounds(0, 0, dp2px(context, 30), dp2px(context, 30))
        imageView.setImageDrawable(drawable)
        return addLeftView(imageView, backId, null) as ImageButton
    }

    fun addLeftBackImageButton(@DrawableRes backDrawableRes: Int, @IdRes backId: Int): View {
        return addLeftBackImageButton(ContextCompat.getDrawable(context, backDrawableRes), backId)
    }

    fun addLeftView(view: View, viewId: Int, layoutParams: LayoutParams? = null): View {
        val generateLayoutParams =
                layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                        .apply {
                            if (leftLastId == DEFAULT_VIEW_ID) {
                                this.addRule(ALIGN_PARENT_LEFT or CENTER_VERTICAL)
                            } else {
                                this.addRule(ALIGN_RIGHT or CENTER_VERTICAL, leftLastId)
                            }
                        }
        leftLastId = viewId
        view.id = viewId
        leftViews.add(view)
        addView(view, generateLayoutParams)
        return view
    }

    fun addRightImageButton(@DrawableRes backDrawableRes: Int, @IdRes backId: Int): View {
        return addRightBackImageButton(ContextCompat.getDrawable(context, backDrawableRes), backId)
    }

    fun addRightBackImageButton(
            drawable: Drawable?,
            @IdRes backId: Int,
            @ColorRes tintColorRes: Int = -1
    ): ImageButton {
        val imageView = FastRoundImageButton(context)
        if (tintColorRes != -1) {
            drawable?.setColorFilter(
                    ContextCompat.getColor(context, tintColorRes),
                    PorterDuff.Mode.SRC_ATOP
            )
        } else {
            drawable?.setColorFilter(
                    titleColor,
                    PorterDuff.Mode.SRC_ATOP
            )
        }
        drawable?.setBounds(0, 0, dp2px(context, 30), dp2px(context, 30))
        imageView.setImageDrawable(drawable)
        return addRightView(imageView, backId, null) as ImageButton
    }

    fun addRightView(view: View, viewId: Int, layoutParams: LayoutParams? = null): View {
        val generateLayoutParams =
                layoutParams ?: LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                        .apply {
                            if (rightLastId == DEFAULT_VIEW_ID) {
                                this.addRule(ALIGN_PARENT_RIGHT)
                                this.addRule(CENTER_VERTICAL)
                            } else {
                                this.addRule(ALIGN_RIGHT, rightLastId)
                                this.addRule(CENTER_VERTICAL)
                            }
                        }
        rightLastId = viewId
        view.id = viewId
        rightViews.add(view)
        addView(view, generateLayoutParams)
        return view
    }
}
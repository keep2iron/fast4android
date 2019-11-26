package io.github.keep2iron.fast4android.core.widget

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.legacy.widget.Space
import io.github.keep2iron.base.util.FastDisplayHelper.dp2px
import io.github.keep2iron.base.util.WeakHandler
import io.github.keep2iron.base.util.assignTextViewWithAttr
import io.github.keep2iron.base.util.getAttrDimen
import io.github.keep2iron.fast4android.R
import io.github.keep2iron.fast4android.core.alpha.FastAlphaRoundButton
import io.github.keep2iron.fast4android.core.alpha.FastAlphaRoundLinearLayout
import io.github.keep2iron.peach.DrawableCreator

typealias FastDialogClickListener = (dialog: Dialog, index: Int, actionProp: Int) -> Unit

class FastDialogContentView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FastAlphaRoundLinearLayout(context, attrs, defStyleAttr) {

    private var minWidth: Int = context.getAttrDimen(R.attr.fast_dialog_min_width)
    private var maxWidth: Int = context.getAttrDimen(R.attr.fast_dialog_max_width)

    init {
        setChangeAlphaWhenPress(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val reWidthMeasureSpec = if (maxWidth in 1 until widthSize) {
            MeasureSpec.makeMeasureSpec(maxWidth, widthMode)
        } else {
            widthMeasureSpec
        }
        super.onMeasure(reWidthMeasureSpec, heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            val measureWidth = measuredWidth
            if (minWidth in (measureWidth + 1) until widthSize) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(minWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
            }
        }
    }

}

class FastDialogAction(block: FastDialogAction.() -> Unit) {

    companion object {
        const val ACTION_PROP_POSITIVE = 0
        const val ACTION_PROP_NEUTRAL = 1
        const val ACTION_PROP_NEGATIVE = 2
    }

    var actionProp: Int = ACTION_PROP_NEUTRAL

    var content: CharSequence = ""

    var actionView: FastAlphaRoundButton? = null

    internal var onClickListener: FastDialogClickListener? = null

    var enabled = true

    init {
        this.block()
    }

    fun generateActionView(dialog: Dialog, index: Int): FastAlphaRoundButton {
        actionView = FastAlphaRoundButton(dialog.context, null, R.attr.fast_dialog_action_style)
        val context = dialog.context

        val nonNullView = actionView
        checkNotNull(nonNullView)
        nonNullView.minHeight = 0
        nonNullView.text = content
        nonNullView.minimumHeight = 0
        nonNullView.setChangeAlphaWhenDisable(true)
        nonNullView.setChangeAlphaWhenPress(true)
        val a = context.obtainStyledAttributes(null, R.styleable.FastDialogActionStyleDef, R.attr.fast_dialog_action_style, 0)
        val count = a.indexCount
        var paddingHor = 0
        var iconSpace = 0
        var negativeTextColor: ColorStateList? = null
        var positiveTextColor: ColorStateList? = null
        for (i in 0 until count) {
            when (val attr = a.getIndex(i)) {
                R.styleable.FastDialogActionStyleDef_android_gravity -> {
                    nonNullView.gravity = a.getInt(attr, -1)
                }
                R.styleable.FastDialogActionStyleDef_android_textColor -> {
                    nonNullView.setTextColor(a.getColorStateList(attr))
                }
                R.styleable.FastDialogActionStyleDef_android_textSize -> {
                    nonNullView.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, 0).toFloat())
                }
                R.styleable.FastDialogActionStyleDef_fast_dialog_action_button_padding_horizontal -> {
                    paddingHor = a.getDimensionPixelSize(attr, 0)
                }
                R.styleable.FastDialogActionStyleDef_android_background -> {
                    nonNullView.background = a.getDrawable(attr)
                }
                R.styleable.FastDialogActionStyleDef_android_minWidth -> {
                    val miniWidth = a.getDimensionPixelSize(attr, 0)
                    nonNullView.minWidth = miniWidth
                    nonNullView.minimumWidth = miniWidth
                }
                R.styleable.FastDialogActionStyleDef_fast_dialog_positive_action_text_color -> {
                    positiveTextColor = a.getColorStateList(attr)
                }
                R.styleable.FastDialogActionStyleDef_fast_dialog_negative_action_text_color -> {
                    negativeTextColor = a.getColorStateList(attr)
                }
                R.styleable.FastDialogActionStyleDef_fast_dialog_action_icon_space -> {
                    iconSpace = a.getDimensionPixelSize(attr, 0)
                }
                R.styleable.FastTextCommonStyleDef_android_textStyle -> {
                    val styleIndex = a.getInt(attr, -1)
                    nonNullView.setTypeface(null, styleIndex)
                }
            }
        }

        a.recycle()
        nonNullView.setPadding(paddingHor, 0, paddingHor, 0)
        nonNullView.isClickable = true
        nonNullView.isEnabled = enabled

        if (actionProp == ACTION_PROP_NEGATIVE) {
            nonNullView.setTextColor(negativeTextColor)
        } else if (actionProp == ACTION_PROP_POSITIVE) {
            nonNullView.setTextColor(positiveTextColor)
        }


        nonNullView.setOnClickListener {
            onClickListener?.invoke(dialog, index, actionProp)
        }
        return nonNullView
    }

}

class FastDialog constructor(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    val weakHandler = WeakHandler()

    init {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let {
            val wmlp = it.attributes
            wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT
            wmlp.gravity = Gravity.CENTER
            it.attributes = wmlp
        }
    }

    fun showDelayDismiss(delayMillis: Long) {
        show()
        weakHandler.postDelayed(Runnable {
            if (isShowing) {
                dismiss()
            }
        }, delayMillis)
    }

}

abstract class AbstractDialogBuilder(val context: Context) {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    var onClickListener: FastDialogClickListener? = null

    var title: CharSequence = ""

    var themeResId: Int = R.style.Fast_Dialog

    var cancelable: Boolean = true

    var canceledOnTouchOutside: Boolean = true

    var actionOrientation: Int = HORIZONTAL

    var changeAlphaForPressOnDisable = true

    var changeAlphaForPressOnPress = true

    var backgroundRadius: Int = 0

    protected val actions = mutableListOf<FastDialogAction>()

    /**
     * when create title
     */
    open fun onCreateTitle(titleView: TextView) {
    }

    abstract fun onCreateMessage(dialog: Dialog, dialogView: ViewGroup, context: Context)

    fun build(): FastDialog {
        val dialog = FastDialog(context, themeResId)
        val contentView = LayoutInflater.from(dialog.context).inflate(R.layout.fast_dialog_layout, null, false)
        val dialogContent = contentView.findViewById<FastAlphaRoundLinearLayout>(R.id.dialogContent)

        if (title.isNotEmpty()) {
            val titleView = TextView(context)
            titleView.text = title
            titleView.assignTextViewWithAttr(R.attr.fast_dialog_title_style)
            dialogContent.addView(titleView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            onCreateTitle(titleView)
        }

        onCreateMessage(dialog, dialogContent, context)

        onCreateActions(dialog, dialogContent, context)

        dialog.setContentView(contentView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        dialog.setCancelable(cancelable)

        if (backgroundRadius > 0f) {
            dialogContent.background = DrawableCreator()
                    .rectangle()
                    .complete()
                    .solidColor(Color.WHITE)
                    .complete()
                    .cornerRadius(backgroundRadius)
                    .complete()
                    .build()
        }

        val clickListener = View.OnClickListener {
            if (cancelable) {
                dialog.dismiss()
            }
        }
        contentView.findViewById<View>(R.id.anchor_top).setOnClickListener(clickListener)
        contentView.findViewById<View>(R.id.anchor_bottom).setOnClickListener(clickListener)
        contentView.setOnClickListener(clickListener)

        return dialog
    }

    /**
     * when create action views
     */
    open fun onCreateActions(dialog: Dialog, dialogView: ViewGroup, context: Context) {
        fun createActionContainerSpace(context: Context): View {
            val space = Space(context)
            val spaceLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            space.layoutParams = spaceLp
            return space
        }

        val size = actions.size
        if (size > 0) {
            val a = context.obtainStyledAttributes(null, R.styleable.FastDialogActionContainerCustomDef, R.attr.fast_dialog_action_container_style, 0)
            val count = a.indexCount
            var justifyContent = 1
            var spaceCustomIndex = 0
            var actionHeight = -1
            var actionSpace = 0
            for (i in 0 until count) {
                when (val attr = a.getIndex(i)) {
                    R.styleable.FastDialogActionContainerCustomDef_fast_dialog_action_container_justify_content -> justifyContent = a.getInteger(attr, justifyContent)
                    R.styleable.FastDialogActionContainerCustomDef_fast_dialog_action_container_custom_space_index -> spaceCustomIndex = a.getInteger(attr, 0)
                    R.styleable.FastDialogActionContainerCustomDef_fast_dialog_action_space -> actionSpace = a.getDimensionPixelSize(attr, 0)
                    R.styleable.FastDialogActionContainerCustomDef_fast_dialog_action_height -> actionHeight = a.getDimensionPixelSize(attr, 0)
                }
            }
            a.recycle()
            var spaceInsertPos = -1// 如果ActionButton的宽度过宽，则减小padding
            // add divider
            when {
                actionOrientation == VERTICAL -> spaceInsertPos = -1
                justifyContent == 0 -> spaceInsertPos = size
                justifyContent == 1 -> spaceInsertPos = 0
                justifyContent == 3 -> spaceInsertPos = spaceCustomIndex
            }


            val mActionContainer = FastAlphaRoundLinearLayout(context, null, R.attr.fast_dialog_action_container_style)
            mActionContainer.orientation = if (actionOrientation == VERTICAL) {
                LinearLayout.VERTICAL
            } else {
                LinearLayout.HORIZONTAL
            }
            mActionContainer.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            for (i in 0 until size) {
                if (spaceInsertPos == i) {
                    mActionContainer.addView(createActionContainerSpace(context))
                }
                val action = actions[i]

                val actionLp: LinearLayout.LayoutParams
                if (actionOrientation == VERTICAL) {
                    actionLp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionHeight)
                } else {
                    actionLp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, actionHeight)
                    if (spaceInsertPos >= 0) {
                        if (i >= spaceInsertPos) {
                            actionLp.leftMargin = actionSpace
                        } else {
                            actionLp.rightMargin = actionSpace
                        }
                    }
                    if (justifyContent == 2) {
                        actionLp.weight = 1f
                    }
                }
                val actionView = action.generateActionView(dialog, i)

//                // add divider
//                if (mActionDividerThickness > 0 && i > 0 && spaceInsertPos != i) {
//                    if (mActionContainerOrientation == VERTICAL) {
//                        actionView.onlyShowTopDivider(mActionDividerInsetStart, mActionDividerInsetEnd,
//                                mActionDividerThickness, ContextCompat.getColor(context, mActionDividerColorRes))
//                    } else {
//                        actionView.onlyShowLeftDivider(mActionDividerInsetStart, mActionDividerInsetEnd,
//                                mActionDividerThickness, ContextCompat.getColor(context, mActionDividerColorRes))
//                    }
//
//                }

                actionView.setChangeAlphaWhenDisable(changeAlphaForPressOnDisable)
                actionView.setChangeAlphaWhenPress(changeAlphaForPressOnPress)
                mActionContainer.addView(actionView, actionLp)
            }

            if (spaceInsertPos == size) {
                mActionContainer.addView(createActionContainerSpace(context))
            }

            if (actionOrientation == HORIZONTAL) {
                mActionContainer.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    val width = right - left
                    val childCount = mActionContainer.childCount
                    if (childCount > 0) {
                        val lastChild = mActionContainer.getChildAt(childCount - 1)
                        // 如果ActionButton的宽度过宽，则减小padding
                        if (lastChild.right > width) {
                            val childPaddingHor = Math.max(0, lastChild.paddingLeft - dp2px(context, 3))
                            for (i in 0 until childCount) {
                                mActionContainer.getChildAt(i).setPadding(childPaddingHor, 0, childPaddingHor, 0)
                            }
                        }
                    }
                }
            }
            dialogView.addView(mActionContainer)

        }
    }

    fun addAction(action: FastDialogAction.() -> Unit) {
        actions.add(FastDialogAction(action))
    }

}

class MessageDialogBuilder(
        context: Context,
        block: MessageDialogBuilder.() -> Unit) : AbstractDialogBuilder(context) {

    var message: CharSequence = ""

    init {
        block()
    }

    override fun onCreateMessage(dialog: Dialog, dialogView: ViewGroup, context: Context) {
        if (message.isNotEmpty()) {
            val messageTextView = TextView(context)
            messageTextView.text = message
            messageTextView.assignTextViewWithAttr(R.attr.fast_dialog_message_content_style)
            dialogView.addView(messageTextView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        } else {
            val a = dialogView.context.obtainStyledAttributes(null,
                    R.styleable.FastDialogTitleTvCustomDef, R.attr.fast_dialog_title_style, 0)
            val count = a.indexCount
            for (i in 0 until count) {
                val attr = a.getIndex(i)
                if (attr == R.styleable.FastDialogTitleTvCustomDef_fast_paddingBottomWhenNotContent) {
                    dialogView.setPadding(
                            dialogView.paddingLeft,
                            dialogView.paddingTop,
                            dialogView.paddingRight,
                            a.getDimensionPixelSize(attr, dialogView.paddingBottom)
                    )
                }
            }
            a.recycle()
        }
    }

}

//class LoadingDialogBuilder(
//        context: Context,
//        block: LoadingDialogBuilder.() -> Unit
//) : AbstractDialogBuilder(context) {
//
//    var loadingLayoutId: Int = -1
//
//    init {
//        block()
//    }
//
//    override fun onCreateMessage(dialog: Dialog, dialogView: ViewGroup, context: Context) {
//        val loadingView = if (loadingLayoutId == -1) {
//            FastLoadingView(dialog.context, dp2px(dialog.context, 30), Color.WHITE)
//        } else {
//            LayoutInflater.from(dialog.context).inflate(loadingLayoutId, dialogView)
//        }
//
//        dialogView.background = null
////        loadingView.background = DrawableCreator()
////                .cornerRadius(dp2px(dialog.context, backgroundRadius))
////                .solidColor(ContextCompat.getColor(dialog.context, R.color.fast_config_color_50_white))
////                .build()
//
//        dialogView.addView(loadingView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
//            gravity = Gravity.CENTER
//        })
//    }
//
//}
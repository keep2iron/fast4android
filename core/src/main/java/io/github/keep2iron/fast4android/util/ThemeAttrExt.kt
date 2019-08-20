/*
 * Tencent is pleased to support the open source community by making Fast_Android available.
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

package io.github.keep2iron.fast4android.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import io.github.keep2iron.fast4android.arch.R

fun Context.getAttrFloatValue(attrRes: Int): Float {
  val typedValue = TypedValue()
  theme.resolveAttribute(attrRes, typedValue, true)
  return typedValue.float
}

fun Context.getAttrColor(attrRes: Int): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(attrRes, typedValue, true)
  return typedValue.data
}

fun Context.getAttrColorStateList(attrRes: Int): ColorStateList? {
  val typedValue = TypedValue()
  theme.resolveAttribute(attrRes, typedValue, true)
  return ContextCompat.getColorStateList(this, typedValue.resourceId)
}

fun Context.getAttrDrawable(attrRes: Int): Drawable? {
  val attrs = intArrayOf(attrRes)
  val ta = obtainStyledAttributes(attrs)
  val drawable = getAttrDrawable(this, ta, 0)
  ta.recycle()
  return drawable
}

fun Context.getAttrDrawable(context: Context, typedArray: TypedArray, index: Int): Drawable? {
  val value = typedArray.peekValue(index)
  if (value != null) {
    if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
      return AppCompatResources.getDrawable(context, value.resourceId);
    }
  }
  return null
}

fun Context.getAttrDimen(attrRes: Int): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(attrRes, typedValue, true)
  return TypedValue.complexToDimensionPixelSize(
    typedValue.data,
    resources.displayMetrics
  )
}

fun TextView.assignTextViewWithAttr(attrRes: Int) {
  val a =
    context.obtainStyledAttributes(null, R.styleable.FastTextCommonStyleDef, attrRes, 0)
  val count = a.indexCount
  var paddingLeft = 0
  var paddingRight = 0
  var paddingTop = 0
  var paddingBottom = 0
  for (i in 0 until count) {
    val attr = a.getIndex(i)
    when (attr) {
      R.styleable.FastTextCommonStyleDef_android_gravity -> gravity = a.getInt(attr, -1)
      R.styleable.FastTextCommonStyleDef_android_textColor -> setTextColor(a.getColorStateList(attr))
      R.styleable.FastTextCommonStyleDef_android_textSize -> setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, 0).toFloat())
      R.styleable.FastTextCommonStyleDef_android_paddingLeft -> paddingLeft = a.getDimensionPixelSize(attr, 0)
      R.styleable.FastTextCommonStyleDef_android_paddingRight -> paddingRight = a.getDimensionPixelSize(attr, 0)
      R.styleable.FastTextCommonStyleDef_android_paddingTop -> paddingTop = a.getDimensionPixelSize(attr, 0)
      R.styleable.FastTextCommonStyleDef_android_paddingBottom -> paddingBottom = a.getDimensionPixelSize(attr, 0)
      R.styleable.FastTextCommonStyleDef_android_singleLine -> setSingleLine(a.getBoolean(attr, false))
      R.styleable.FastTextCommonStyleDef_android_ellipsize -> {
        val ellipsize = a.getInt(attr, 3)
        setEllipsize(
          when (ellipsize) {
            1 -> TextUtils.TruncateAt.START
            2 -> TextUtils.TruncateAt.MIDDLE
            3 -> TextUtils.TruncateAt.END
            4 -> TextUtils.TruncateAt.MARQUEE
            else -> throw IllegalArgumentException("not support ellipsize $ellipsize")
          }
        )
      }
      R.styleable.FastTextCommonStyleDef_android_maxLines -> maxLines = a.getInt(attr, -1)
      R.styleable.FastTextCommonStyleDef_android_background -> setBackgroundKeepingPadding(a.getDrawable(attr))
      R.styleable.FastTextCommonStyleDef_android_lineSpacingExtra -> setLineSpacing(a.getDimensionPixelSize(attr, 0).toFloat(), 1f)
      R.styleable.FastTextCommonStyleDef_android_drawablePadding -> compoundDrawablePadding = a.getDimensionPixelSize(attr, 0)
      R.styleable.FastTextCommonStyleDef_android_textColorHint -> setHintTextColor(a.getColor(attr, 0))
      R.styleable.FastTextCommonStyleDef_android_textStyle -> {
        val styleIndex = a.getInt(attr, -1)
        setTypeface(null, styleIndex)
      }
    }
  }
  setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
  a.recycle()
}

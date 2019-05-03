/*
 * Create bt Keep2iron on 17-7-5 下午5:16
 */

package io.github.keep2iron.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import io.github.keep2iron.android.comp.R;

/**
 * 可以控制drawable大小的TextView
 */
public class CompTextViewPlus extends AppCompatTextView {
    class Size {
        int width;
        int height;
    }

    public CompTextViewPlus(Context context) {
        this(context, null);
    }

    public CompTextViewPlus(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompTextViewPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getResources().obtainAttributes(attrs, R.styleable.CompTextViewPlus);

        Size sLeftSize = new Size();
        Size sRightSize = new Size();
        Size sTopSize = new Size();
        Size sBottomSize = new Size();
        int defValue = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        for (int i = 0; i < array.getIndexCount(); i++) {
            int index = array.getIndex(i);
            if (R.styleable.CompTextViewPlus_compDrawableLeftWidth == index) {
                sLeftSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableLeftHeight == index) {
                sLeftSize.height = (int) array.getDimension(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableRightWidth == index) {
                sRightSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableRightHeight == index) {
                sRightSize.height = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableTopWidth == index) {
                sTopSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableTopHeight == index) {
                sTopSize.height = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableBottomWidth == index) {
                sBottomSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.CompTextViewPlus_compDrawableBottomHeight == index) {
                sBottomSize.height = array.getDimensionPixelSize(index, defValue);
            }
        }
        array.recycle();

        Drawable[] compoundDrawables = getCompoundDrawables();

        if (compoundDrawables[0] != null) {                                       //left
            compoundDrawables[0].setBounds(0, 0, sLeftSize.width, sLeftSize.height);
        }

        if (compoundDrawables[1] != null) {                                //top
            compoundDrawables[1].setBounds(0, 0, sTopSize.width, sTopSize.height);
        }

        if (compoundDrawables[2] != null) {                                //right
            compoundDrawables[2].setBounds(0, 0, sRightSize.width, sRightSize.height);
        }

        if (compoundDrawables[3] != null) {                                //bottom
            compoundDrawables[3].setBounds(0, 0, sBottomSize.width, sBottomSize.height);
        }

        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }
}
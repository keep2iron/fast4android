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
public class TextViewPlus extends AppCompatTextView {
    class Size {
        int width;
        int height;
    }

    public TextViewPlus(Context context) {
        this(context, null);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getResources().obtainAttributes(attrs, R.styleable.TextViewPlus);

        Size sLeftSize = new Size();
        Size sRightSize = new Size();
        Size sTopSize = new Size();
        Size sBottomSize = new Size();
        int defValue = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        for (int i = 0; i < array.getIndexCount(); i++) {
            int index = array.getIndex(i);
            if (R.styleable.TextViewPlus_drawableLeftWidth == index) {
                sLeftSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableLeftHeight == index) {
                sLeftSize.height = (int) array.getDimension(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableRightWidth == index) {
                sRightSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableRightHeight == index) {
                sRightSize.height = array.getDimensionPixelSize(index, defValue);
                break;
            } else if (R.styleable.TextViewPlus_drawableTopWidth == index) {
                sTopSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableTopHeight == index) {
                sTopSize.height = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableBottomWidth == index) {
                sBottomSize.width = array.getDimensionPixelSize(index, defValue);
            } else if (R.styleable.TextViewPlus_drawableBottomHeight == index) {
                sBottomSize.height = array.getDimensionPixelSize(index, defValue);
            }
        }
        array.recycle();

        Drawable[] compoundDrawables = getCompoundDrawables();

        if (compoundDrawables[0] != null)

        {                                       //left
            compoundDrawables[0].setBounds(0, 0, sLeftSize.width, sLeftSize.height);
        }

        if (compoundDrawables[1] != null)

        {                                //top
            compoundDrawables[1].setBounds(0, 0, sTopSize.width, sTopSize.height);
        }

        if (compoundDrawables[2] != null)
        {                                //right
            compoundDrawables[2].setBounds(0, 0, sRightSize.width, sRightSize.height);
        }

        if (compoundDrawables[3] != null) {                                //bottom
            compoundDrawables[3].setBounds(0, 0, sBottomSize.width, sBottomSize.height);
        }

        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }
}
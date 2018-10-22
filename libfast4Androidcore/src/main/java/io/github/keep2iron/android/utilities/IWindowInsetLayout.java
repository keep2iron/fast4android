package io.github.keep2iron.android.utilities;

import android.graphics.Rect;
import android.support.v4.view.WindowInsetsCompat;

/**
 * copy from qmui
 *
 * @author keep2ron
 * @date 2018-03-01
 */
public interface IWindowInsetLayout {
    boolean applySystemWindowInsets19(Rect insets);

    boolean applySystemWindowInsets21(WindowInsetsCompat insets);
}
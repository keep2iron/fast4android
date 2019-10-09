package io.github.keep2iron.fast4android.arch.swipe.transform;

import android.graphics.Canvas;
import android.view.View;

import io.github.keep2iron.fast4android.arch.swipe.ParallaxBackLayout;

/**
 * ParallaxBackLayout
 *
 * @author An Zewei (anzewei88[at]gmail[dot]com)
 * @since ${VERSION}
 */

public interface ITransform {
    void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child);
}

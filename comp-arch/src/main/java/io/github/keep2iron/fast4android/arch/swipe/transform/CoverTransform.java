package io.github.keep2iron.fast4android.arch.swipe.transform;

import android.graphics.Canvas;
import android.view.View;

import io.github.keep2iron.fast4android.arch.swipe.ParallaxBackLayout;

import static androidx.customview.widget.ViewDragHelper.EDGE_BOTTOM;
import static androidx.customview.widget.ViewDragHelper.EDGE_LEFT;
import static androidx.customview.widget.ViewDragHelper.EDGE_RIGHT;
import static androidx.customview.widget.ViewDragHelper.EDGE_TOP;

/**
 * ParallaxBackLayout
 *
 * @author An Zewei (anzewei88[at]gmail[dot]com)
 * @since ${VERSION}
 */

public class CoverTransform implements ITransform {
  @Override
  public void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child) {
    int edge = parallaxBackLayout.getEdgeFlag();
    if (edge == EDGE_LEFT) {
      canvas.clipRect(0, 0, child.getLeft(), child.getBottom());
    } else if (edge == EDGE_TOP) {
      canvas.clipRect(0, 0, child.getRight(), child.getTop() + parallaxBackLayout.getSystemTop());
    } else if (edge == EDGE_RIGHT) {
      canvas.clipRect(child.getRight(), 0, parallaxBackLayout.getWidth(), child.getBottom());
    } else if (edge == EDGE_BOTTOM) {
      canvas.clipRect(0, child.getBottom(), child.getRight(), parallaxBackLayout.getHeight());
    }
  }
}

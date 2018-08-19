package io.github.keep2iron.app.widget

import android.graphics.Path

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/05/05 12:28
 */
class BezierCircle {
    var x: Float = 0f
    var y: Float = 0f
    var r: Float = 0f

    var path: Path = Path()

    constructor(index: Int) {
        val step = 40
        val maxRadius = step * 2f
        val minRadius = step * 1.2f
        val maxSpeed = 5f
        val minSpeed = 1.3f
//        circle.r = (Math.random() * (maxRadius - minRadius) + minRadius).toFloat()
//        circle.v = (Math.random() * (maxSpeed - minSpeed) + minSpeed).toFloat()
//        circle.x = (i + Math.random() * step).toFloat()
//        circle.y = 0f
    }

    fun action() {

        path.reset()
        path.moveTo(0f,0f)

    }
}
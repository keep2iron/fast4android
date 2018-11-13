package io.github.keep2iron.android.load

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/11/13
 *
 * 该类用于进行分页操作
 */
class Pager(var defaultValue: Any) {

    var value: Any = defaultValue

    fun reset() {
        this.value = defaultValue
    }
}
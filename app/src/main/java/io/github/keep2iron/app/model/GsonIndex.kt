package io.github.keep2iron.app.model

/**
 * @author keep2iron [Contract me.](http://keep2iron.github.io)
 * @version 1.0
 * @since 2018/03/23 16:09
 */
class GsonIndex {

    var id: String? = null
    var imageUrl: String? = null
    var title: String? = null
    val type: Int = 0
    var description: String? = null
    var labels: List<String>? = null

    fun labelsConvert2labelString(): String {
        val stringBuilder = StringBuilder()
        labels?.forEach {
            stringBuilder.append("·")
                    .append(it)
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.deleteCharAt(0)
        }
        return stringBuilder.toString()
    }

    companion object {

        /**
         * 文章
         */
        var TYPE_ARTICLE = 1
        /**
         * 视频
         */
        var TYPE_VIDEO = 2
        /**
         * 轮播图
         */
        var TYPE_BANNER = 3
    }

}
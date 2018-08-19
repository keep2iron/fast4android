package io.github.keep2iron.android.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

/**
 * @author 薛世君
 * @date 2017/2/14
 *
 *
 * 常用工具类
 */
class CommonUtil {
    private lateinit var context: Context

    private constructor()

    companion object {
        fun getInstance(context: Context): CommonUtil {
            return CommonUtil(context)
        }
    }

    constructor(context: Context) {
        this.context = context.applicationContext
    }

    val screenWidth: Int
        get() = context.resources.displayMetrics.widthPixels


    val screenHeight: Int
        get() = context.resources.displayMetrics.heightPixels

    fun dp2px(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun px2dp(px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }

    fun getDrawable(drawable: Int): Drawable {
        return context.resources.getDrawable(drawable)
    }

    fun getDp(value: Int): Float {
        return context.resources.displayMetrics.density * value
    }

    fun getDrawable(drawableId: Int, @DimenRes widthSize: Int, @DimenRes heightSize: Int): Drawable {
        val width = getPercentageSize(widthSize)
        val height = getPercentageSize(heightSize)
        val drawable = getDrawable(drawableId)
        drawable.setBounds(0, 0, width.toInt(), height.toInt())

        return drawable
    }

    /**
     * 获取百分比的大小
     *
     * @param id R.dimen.x1
     * @return 百分比尺寸
     */
    fun getPercentageSize(@DimenRes id: Int): Float {
        return context.resources.getDimension(id)
    }

    fun getColor(@ColorRes colorRes: Int): Int {
        return context.resources.getColor(colorRes)
    }

    fun getColor(colorString: String): Int {
        return Color.parseColor(colorString)
    }

    fun isCollectionNotEmpty(collection: Collection<*>?): Boolean {
        return collection != null && !collection.isEmpty()
    }

    fun isCollectionEmpty(collection: Collection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    fun isStringMatch(s1: String?, s2: Any?): Boolean {
        return if (s1 == null || s2 == null) {
            false
        } else s1 == s2

    }

    fun isEmpty(string: String?): Boolean {
        return string == null || string.trim { it <= ' ' }.isEmpty()
    }

    /**
     * 讲秒格式化为指定的字符串
     *
     * @param second       秒
     * @param formatString 格式化字符串
     * @return 格式化后字符串
     */
    fun formatTimeWithSecond(second: Long, formatString: String): String {
        val sdf = SimpleDateFormat(formatString, Locale.CHINESE)
        return sdf.format(Date(second * 1000))
    }

    fun parseTimeString(string: String, formatString: String): Date? {
        val sdf = SimpleDateFormat(formatString, Locale.CHINESE)
        try {
            return sdf.parse(string)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun formatTime(time: Long, formatString: String): String {
        val sdf = SimpleDateFormat(formatString, Locale.CHINESE)
        return sdf.format(Date(time))
    }

    fun getStringRes(@StringRes stringResId: Int): String {
        return context.resources.getString(stringResId)
    }

    fun parseUrl2Map(URL: String): Map<String, String> {
        val mapRequest = HashMap<String, String>()
        var arrSplit: Array<String>? = null
        val strUrlParam = TruncateUrlPage(URL)
        Log.e("info", "strUrlParam==================================" + strUrlParam)
        if (strUrlParam == null) {
            return mapRequest
        }//每个键值为一组
        arrSplit = strUrlParam.split("[&]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (strSplit in arrSplit) {
            var arrSplitEqual: Array<String>? = null
            //解析出键值
            arrSplitEqual = strSplit.split("[=]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //正确解析
            if (arrSplitEqual.size > 1) {
                mapRequest[arrSplitEqual[0]] = arrSplitEqual[1]
            } else {
                //只有参数没有值，不加入
                if (arrSplitEqual[0] !== "") {
                    mapRequest[arrSplitEqual[0]] = ""
                }
            }
        }
        return mapRequest
    }

    private fun TruncateUrlPage(strURL: String): String {
        var strURL = strURL
        strURL = strURL.trim { it <= ' ' }.toLowerCase()
        return if (strURL.indexOf("?") != -1) {
            strURL.substring(strURL.indexOf("?") + 1)
        } else strURL
    }

    fun openSoftInput(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun closeSoftInput(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun convertFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }
}
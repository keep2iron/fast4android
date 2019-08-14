package io.github.keep2iron.fast4android.utilities

import androidx.collection.ArrayMap
import java.text.SimpleDateFormat
import java.util.Date

private val patternMap by lazy(LazyThreadSafetyMode.NONE) {
  val arrayMap = androidx.collection.ArrayMap<String, SimpleDateFormat>()
  arrayMap[DatePattern.YYYY_MM_DD.pattern] = SimpleDateFormat(DatePattern.YYYY_MM_DD.pattern)
  arrayMap[DatePattern.YYYY_MM_DD_HH_MM.pattern] =
    SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM.pattern)
  return@lazy arrayMap
}

enum class DatePattern(val pattern: String) {
  /**
   * exp:2018-01-01
   */
  YYYY_MM_DD("YYYY-MM-DD"),

  /**
   * exp:2018-01-01 01:01
   */
  YYYY_MM_DD_HH_MM("YYYY-MM-DD HH:mm"),
}

/**
 * 格式化时间 YYYY-MM-dd
 */
fun Long.simpleformatDate(): String {
  val simpleDateFormat = patternMap[DatePattern.YYYY_MM_DD.pattern]
  return simpleDateFormat!!.format(Date(this))
}

/**
 * 格式化时间 YYYY-MM-DD HH:mm
 */
fun Long.formatDate(): String {
  val simpleDateFormat = patternMap[DatePattern.YYYY_MM_DD_HH_MM.pattern]
  return simpleDateFormat!!.format(Date(this))
}

/**
 * 自定义格式化时间
 */
fun Long.formatDatePattern(pattern: String): String {
  var simpleDateFormat = patternMap[pattern]
  if (simpleDateFormat == null) {
    simpleDateFormat = SimpleDateFormat(pattern)
    patternMap[pattern] = simpleDateFormat
  }

  return simpleDateFormat.format(pattern)
}
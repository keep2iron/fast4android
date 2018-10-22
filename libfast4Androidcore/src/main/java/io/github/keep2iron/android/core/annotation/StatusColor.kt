/*
 * Create bt Keep2iron on 17-5-25 下午3:47
 * Copyright (c) 2017. All rights reserved.
 */

package io.github.keep2iron.android.core.annotation

import android.support.annotation.ColorRes

/**
 * Created by 薛世君 on 2017/2/13.
 *
 *
 * 该注解是用来进行注解状态栏的颜色的
 *
 * value用于定义状态栏的颜色值
 * isDarkMode用于定义状态栏的字体颜色 true 黑色 false 白色
 * isTrans是否是透明状态栏
 * isFitSystem 状态栏是否占用空间
 *
 * isTrans=true && isFitSystem=true时，会占据状态栏的位置
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class StatusColor(@ColorRes val value: Int = -1,
                             val isDarkMode: Boolean = false,
                             val isTrans: Boolean = false,
                             val isFitSystem: Boolean = true,
                             val navigationBarColor:Int  = -1)
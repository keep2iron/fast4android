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
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class StatusColor(@ColorRes val value: Int = -1, val isDarkMode: Boolean = false, val isTrans: Boolean = false)
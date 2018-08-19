package io.github.keep2iron.android.core.annotation

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2018/07/08 10:04
 *
 * 用于标志一个Component组件
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Component(val serviceName: String)
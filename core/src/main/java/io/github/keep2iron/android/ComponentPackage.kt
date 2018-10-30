package io.github.keep2iron.android

/**
 *
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @date 2018/10/30
 */
interface ComponentPackage {

    fun createComponentModuleProvider(): List<ComponentModuleProvider>

    fun createComponentServiceProvider(): List<ComponentServiceProvider<*>>

}
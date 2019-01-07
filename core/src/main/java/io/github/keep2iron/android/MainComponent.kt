package io.github.keep2iron.android

/**
 * Application实现该接口
 */
interface MainComponent {

    fun createComponentModuleProvider(): List<ComponentModuleProvider>

    fun createComponentServiceProvider(): List<ComponentServiceProvider<*>>

}
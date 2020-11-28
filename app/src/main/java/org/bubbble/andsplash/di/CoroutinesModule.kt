package org.bubbble.andsplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.bubbble.andsplash.shared.di.DefaultDispatcher
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.di.MainDispatcher
import org.bubbble.andsplash.shared.di.MainImmediateDispatcher

/**
 * @author Andrew
 * @date 2020/10/20 11:20
 * @Module 它会告知 Hilt 如何提供某些类型的实例。
 * 您必须使用 @InstallIn 为 Hilt 模块添加注释，以告知 Hilt 每个模块将用在或安装在哪个 Android 类中。
 *
 * 然后，Hilt 需要知道如何提供与每个限定符对应的类型的实例。
 * 在这种情况下，您可以使用带有 @Provides 的 Hilt 模块。这两种方法具有相同的返回类型，
 * 但限定符将它们标记为两个不同的绑定：
 *
 * -------------------------------------------
 *
 * 也就是说，假如有个参数用了我们这个自定义的注解，Hilt生成这个参数依赖的方式由我们这里的写法而定。
 * 比如这里的IoDispatcher，用了这个注解，Hilt就会返回一个 Dispatchers.IO，
 * 但它们的类型都是CoroutineDispatcher，也就是说依赖的是一个CoroutineDispatcher
 * 用了哪个注解，就会用哪个生成依赖。
 */
@InstallIn(ApplicationComponent::class)
@Module
object CoroutinesModule {

    /**
     * @Provides 注入实例
     * 函数返回类型会告知 Hilt 函数提供哪个类型的实例。
     * 函数参数会告知 Hilt 相应类型的依赖项。
     * 函数主体会告知 Hilt 如何提供相应类型的实例。每当需要提供该类型的实例时，Hilt 都会执行函数主体。
     */
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
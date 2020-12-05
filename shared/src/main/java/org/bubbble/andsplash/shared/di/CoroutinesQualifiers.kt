package org.bubbble.andsplash.shared.di

/**
 * @author Andrew
 * @date 2020/10/19 9:41
 */

import javax.inject.Qualifier

/*
 @Qualifier 用于标识限定器注解。任何人都可以定义新的限定器注解。
 限定器注解用于标注可注入的字段或参数，外加该字段或参数的类型，就可以标识出待注入的实现。
 限定符是可选的，当与 @Inject 一起使用在与注入器无关的类时，对于一个字段或参数，应该最多只有一个限定符被标注

 @Retention BINARY是指编译成二进制文件阶段

 注解是将元数据附加到代码的方法。要声明注解，请将 annotation 修饰符放在类的前面
 */

// 首先，定义要用于为 @Binds 或 @Provides 方法添加注释的限定符：

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScope

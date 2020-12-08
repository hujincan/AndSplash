package org.bubbble.andsplash.ui.signin

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.bubbble.andsplash.shared.domain.auth.ObserveUserAuthStateCoroutineUseCase
import org.bubbble.andsplash.shared.domain.user.UserInfoUpdateUseCase
import javax.inject.Singleton

/**
 * @author Andrew
 * @date 2020/10/25 11:03
 */

@InstallIn(ApplicationComponent::class)
@Module
class SignInViewModelDelegateModule {

    /**
     * 需要它是单例的注入依赖，所以这里定义了构建器
     */
    @Singleton
    @Provides
    fun provideSignInViewModelDelegate(
        observerUserAuthStateUseCase: ObserveUserAuthStateCoroutineUseCase,
        userInfoUpdateUseCase: UserInfoUpdateUseCase,
        @ApplicationContext context: Context
    ): SignInViewModelDelegate {
        return UnsplashSignInViewModelDelegate(
            observerUserAuthStateUseCase = observerUserAuthStateUseCase,
            userInfoUpdateUseCase = userInfoUpdateUseCase,
            context = context
        )
    }
}
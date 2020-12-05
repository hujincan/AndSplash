package org.bubbble.andsplash.ui.signin

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.signin.AuthorizeRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.auth.ObserveUserAuthStateCoroutineUseCase
import javax.inject.Singleton

/**
 * @author Andrew
 * @date 2020/10/25 11:03
 */

@InstallIn(ApplicationComponent::class)
@Module
class SignInViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideSignInViewModelDelegate(
        observerUserAuthStateUseCase: ObserveUserAuthStateCoroutineUseCase,
        @ApplicationContext context: Context
    ): SignInViewModelDelegate {
        return GoogleSignInViewModelDelegate(
            observerUserAuthStateUseCase = observerUserAuthStateUseCase,
            context = context
        )
    }

    @Singleton
    @Provides
    fun provideObserveUserAuthStateUseCase(
        repository: AuthorizeRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): ObserveUserAuthStateCoroutineUseCase {
        return ObserveUserAuthStateCoroutineUseCase(repository, ioDispatcher)
    }
}
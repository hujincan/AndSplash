package org.bubbble.andsplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import org.bubbble.andsplash.util.signin.SignInHandler
import org.bubbble.andsplash.shared.di.ApplicationScope
import org.bubbble.andsplash.util.signin.UnsplashAuthSignInHandler

/**
 * @author Andrew
 * @date 2020/10/20 16:55
 */
@InstallIn(ApplicationComponent::class)
@Module
internal class SignInModule {
    @Provides
    fun provideSignInHandler(
        @ApplicationScope applicationScope: CoroutineScope,
    ): SignInHandler = UnsplashAuthSignInHandler(applicationScope)
}
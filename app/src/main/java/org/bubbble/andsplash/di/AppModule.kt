package org.bubbble.andsplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bubbble.andsplash.shared.di.ApplicationScope
import org.bubbble.andsplash.shared.di.DefaultDispatcher
import javax.inject.Singleton

/**
 * @author Andrew
 * @date 2020/10/21 14:37
 */
@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

}
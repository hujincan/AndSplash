package org.bubbble.andsplash.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.di.ApplicationScope
import org.bubbble.andsplash.shared.di.DefaultDispatcher
import org.bubbble.andsplash.shared.util.logger
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

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        logger("数据库正在初始化")
        return AppDatabase.buildDatabase(context)
    }

}
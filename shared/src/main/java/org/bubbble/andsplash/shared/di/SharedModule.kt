package org.bubbble.andsplash.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.signin.AuthorizeRepository
import org.bubbble.andsplash.shared.data.signin.DefaultAuthorizeRepository
import org.bubbble.andsplash.shared.data.user.DefaultUserDataRepository
import org.bubbble.andsplash.shared.data.user.UserDataRepository
import org.bubbble.andsplash.shared.network.service.AuthorizeService
import org.bubbble.andsplash.shared.network.service.UserInfoService
import org.bubbble.andsplash.shared.util.PreferencesUtil
import javax.inject.Singleton

/**
 * @author Andrew
 * @date 2020/10/21 16:23
 */
@InstallIn(ApplicationComponent::class)
@Module
class SharedModule {

    @Singleton
    @Provides
    fun provideAgendaRepository(
        service: AuthorizeService,
        preferencesUtil: PreferencesUtil
    ): AuthorizeRepository =
        DefaultAuthorizeRepository(service, preferencesUtil)

    @Singleton
    @Provides
    fun provideUserDataRepository(
        service: UserInfoService,
        appDatabase: AppDatabase
    ): UserDataRepository =
        DefaultUserDataRepository(service, appDatabase)

}
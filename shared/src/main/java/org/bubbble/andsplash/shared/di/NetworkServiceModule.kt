package org.bubbble.andsplash.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bubbble.andsplash.shared.network.interceptor.AuthInterceptor
import org.bubbble.andsplash.shared.network.service.AuthorizeService
import org.bubbble.andsplash.shared.network.service.UserInfoService
import org.bubbble.andsplash.shared.util.PreferencesUtil
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Andrew
 * @date 2020/12/05 21:09
 */
@InstallIn(ApplicationComponent::class)
@Module
class NetworkServiceModule {

    @Provides
    fun getAuthorizeService(
        @ApplicationInstance client: OkHttpClient,
        @ApplicationInstance gsonConverterFactory: GsonConverterFactory
    ) = AuthorizeService(client, gsonConverterFactory)

    @Provides
    fun getUserInfoService(
        @ApplicationInstance client: OkHttpClient,
        @ApplicationInstance gsonConverterFactory: GsonConverterFactory,
        authInterceptor: AuthInterceptor
    ) = UserInfoService(client, gsonConverterFactory, authInterceptor)

    @Provides
    fun getAuthInterceptor(preferencesUtil: PreferencesUtil) = AuthInterceptor(preferencesUtil)
}
package org.bubbble.andsplash.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import org.bubbble.andsplash.shared.network.service.AuthorizeService
import retrofit2.converter.gson.GsonConverterFactory

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
    )  = AuthorizeService(client, gsonConverterFactory)

}
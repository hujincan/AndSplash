package org.bubbble.andsplash.shared.network.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bubbble.andsplash.model.user.MeInfo
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.network.api.UserApi
import org.bubbble.andsplash.shared.network.interceptor.AuthInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Andrew
 * @date 2020/12/08 9:48
 */
class UserInfoService (
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    authInterceptor: AuthInterceptor) {

    private var api = Retrofit.Builder()
        .baseUrl(ConnectionURL.UNSPLASH_API_BASE_URL)
        .client(client.newBuilder().apply {
            if (org.bubbble.andsplash.shared.BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            }
            addInterceptor(authInterceptor)
        }.build())
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(UserApi::class.java)

    suspend fun requestUserInfo (): Response<MeInfo> = api.getMeInfo()
}
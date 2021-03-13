package org.bubbble.andsplash.shared.network.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.network.api.AuthorizeApi
import org.bubbble.andsplash.shared.network.interceptor.NapiInterceptor
import org.bubbble.andsplash.shared.util.logger
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Andrew
 * @date 2020/12/05 20:26
 */
class AuthorizeService (
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
){
    private var api = Retrofit.Builder()
        .baseUrl(ConnectionURL.UNSPLASH_URL)
        .client(client.newBuilder().apply {
            if (org.bubbble.andsplash.shared.BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            }
        }.build())
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(AuthorizeApi::class.java)

    suspend fun requestAccessToken(
        code: String?
    ): Response<AccessToken> {
        return api.getAccessToken(
            BuildConfig.ACCESS_KEY,
            BuildConfig.SECRET_KEY,
            "andsplash://" + ConnectionURL.UNSPLASH_LOGIN_CALLBACK,
            code,
            "authorization_code"
        )
    }
}
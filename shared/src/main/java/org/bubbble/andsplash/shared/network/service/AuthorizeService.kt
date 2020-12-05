package org.bubbble.andsplash.shared.network.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.network.api.AuthorizeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Andrew
 * @date 2020/12/05 20:26
 */
class AuthorizeService(
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
){

    private var api = Retrofit.Builder()
        .baseUrl(ConnectionURL.UNSPLASH_URL)
        .client(client.newBuilder().build())
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(AuthorizeApi::class.java)

    fun requestAccessToken(
        code: String?
    ): AccessToken {
        return api.getAccessToken(
            BuildConfig.ACCESS_KEY,
            BuildConfig.SECRET_KEY,
            "andsplash://" + ConnectionURL.UNSPLASH_LOGIN_CALLBACK,
            code,
            "authorization_code"
        )
    }
}
package org.bubbble.andsplash.shared.network.api

import androidx.lifecycle.MutableLiveData
import org.bubbble.andsplash.model.AccessToken
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Andrew
 * @date 2020/12/05 19:28
 */
interface AuthorizeApi {

    @POST("oauth/token")
    fun getAccessToken(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("redirect_uri") redirect_uri: String,
        @Query("code") code: String?,
        @Query("grant_type") grant_type: String,
    ): AccessToken
}
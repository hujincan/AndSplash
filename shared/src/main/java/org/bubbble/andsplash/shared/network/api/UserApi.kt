package org.bubbble.andsplash.shared.network.api

import org.bubbble.andsplash.model.user.UserInfo
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Andrew
 * @date 2020/12/08 11:30
 */
interface UserApi {

    @GET("me")
    suspend fun getUserInfo() : Response<UserInfo>
}
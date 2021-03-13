package org.bubbble.andsplash.shared.network.api

import org.bubbble.andsplash.model.user.MeInfo
import org.bubbble.andsplash.model.user.UserInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Andrew
 * @date 2020/12/08 11:30
 */
interface UserApi {

    @GET("me")
    suspend fun getMeInfo(): Response<MeInfo>

    @PUT("me")
    suspend fun putMeInfo(
        @Query("username") username: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name: String,
        @Query("email") email: String,
        @Query("url") url: String,
        @Query("location") location: String,
        @Query("bio") bio: String,
        @Query("instagram_username") instagram_username: String
    ): Response<MeInfo>

    @GET("users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String
    ): Response<UserInfo>
}
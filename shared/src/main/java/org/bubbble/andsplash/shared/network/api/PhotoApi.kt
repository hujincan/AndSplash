package org.bubbble.andsplash.shared.network.api

import org.bubbble.andsplash.model.photo.CollectionsInfo
import org.bubbble.andsplash.model.photo.PhotoInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Andrew
 * @date 2020/12/21 15:15
 */
interface PhotoApi {

    @GET("photos")
    fun getAllPhotos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String,
    ): Response<List<PhotoInfo>>

    @GET("photos/{id}")
    fun getPhotoInfo(
        @Path("id") id: String
    ): Response<PhotoInfo>

    @GET("users/{username}/photos")
    fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String
    ): Response<List<PhotoInfo>>

    @GET("user/{username}/likes")
    fun getUserLikePhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String
    ): Response<List<PhotoInfo>>

    @GET("user/{username}/collections")
    fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<List<CollectionsInfo>>
}
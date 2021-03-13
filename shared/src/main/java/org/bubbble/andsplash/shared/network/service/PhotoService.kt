package org.bubbble.andsplash.shared.network.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bubbble.andsplash.model.photo.PhotoInfo
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.network.api.PhotoApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Andrew
 * @date 2021/02/06 16:43
 */
class PhotoService (
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory ){

    private var api = Retrofit.Builder()
        .baseUrl(ConnectionURL.UNSPLASH_URL)
        .client(client.newBuilder().apply {
            if (org.bubbble.andsplash.shared.BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            }
        }.build())
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(PhotoApi::class.java)

    suspend fun getAllPhoto(
        page: Int,
        per_page: Int,
        order_by: String
    ): Response<List<PhotoInfo>> {
        return api.getAllPhotos(
            page,
            per_page,
            order_by
        )
    }
}
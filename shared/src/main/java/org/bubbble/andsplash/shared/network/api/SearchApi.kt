package org.bubbble.andsplash.shared.network.api

import org.bubbble.andsplash.model.photo.CollectionsInfo
import org.bubbble.andsplash.model.photo.PhotoInfo
import org.bubbble.andsplash.model.search.SearchPhotosInfo
import org.bubbble.andsplash.model.user.UserInfo
import retrofit2.Response

/**
 * @author Andrew
 * @date 2020/12/22 13:45
 */
interface SearchApi {

    fun searchPhotos(): Response<List<SearchPhotosInfo>>

    fun searchUsers(): Response<List<UserInfo>>

    fun searchCollections(): Response<List<CollectionsInfo>>
}
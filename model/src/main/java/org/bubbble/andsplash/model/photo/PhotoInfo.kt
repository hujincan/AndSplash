package org.bubbble.andsplash.model.photo

import android.nfc.Tag
import org.bubbble.andsplash.model.user.UserInfo


/**
 * @author Andrew
 * @date 2020/12/21 15:16
 */
data class PhotoInfo(
    val id: String,
    val created_at: String?,
    val updated_at: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val downloads: Int?,
    val likes: Int?,
    val liked_by_user: Boolean?,
    val description: String?,
    val user: UserInfo?,
    val urls: UrlsInfo,
    val location: LocationInfo?,
    val exif: List<ExifInfo>?,
    val current_user_collections: List<CollectionsInfo>?,
    val tags: List<Tag>?,
)
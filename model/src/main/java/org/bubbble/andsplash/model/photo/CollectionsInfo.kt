package org.bubbble.andsplash.model.photo

import org.bubbble.andsplash.model.user.UserInfo

/**
 * @author Andrew
 * @date 2020/12/21 15:50
 */
data class CollectionsInfo(
    val id: String,
    val title: String,
    val published_at: String,
    val last_collected_at: String,
    val updated_at: String,
    val featured: Boolean,
    val private: Boolean,
    val share_key: String,
    val cover_photo: PhotoInfo,
    val user: UserInfo,
    val total_photos: Int
)

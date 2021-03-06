package org.bubbble.andsplash.shared.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author Andrew
 * @date 2020/12/08 10:36
 */
@Entity(tableName = "userentity", indices = [Index(value = ["numeric_id"], unique = true)])
data class UserEntity (
    @PrimaryKey val numeric_id: Int?,
    val username: String?,
    val name: String?,
    val first_name: String?,
    val last_name: String?,
    val location: String?,
    val profile_image: String?,
    val instagram_username: String?,
    val portfolio_url: String?,
    val bio: String?,
    val total_collections: Int?,
    val total_likes: Int?,
    val total_photos: Int?,
    val email: String?,
    val downloads: Int?,
    val access_token: String?
) {
    companion object {
        fun getDefault() = UserEntity(
            null,
            "请先登录",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            0,
            0,
            0,
            "请先登录",
            0,
            null
        )
    }
}
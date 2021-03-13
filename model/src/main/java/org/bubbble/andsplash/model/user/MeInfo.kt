package org.bubbble.andsplash.model.user

/**
 * @author Andrew
 * @date 2020/12/08 11:35
 */
data class MeInfo(
    val numeric_id: Int,
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String,
    val location: String,
    val profile_image: ProfileImage,
    val instagram_username: String,
    val portfolio_url: String,
    val bio: String,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val email: String,
    val downloads: Int
)

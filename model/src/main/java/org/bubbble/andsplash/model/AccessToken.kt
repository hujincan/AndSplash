package org.bubbble.andsplash.model

/**
 * @author Andrew
 * @date 2020/12/05 20:49
 */
data class AccessToken(
    val access_token: String,
    val token_type: String,
    val scope: String,
    val created_at: Int
)

package org.bubbble.andsplash.model.search

import org.bubbble.andsplash.model.user.UserInfo

/**
 * @author Andrew
 * @date 2020/12/22 20:17
 */
data class SearchUsersInfo(
    val total: Long,
    val total_pages: Long,
    val results: List<UserInfo>,

)

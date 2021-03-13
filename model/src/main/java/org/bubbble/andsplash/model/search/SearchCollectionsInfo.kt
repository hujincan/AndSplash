package org.bubbble.andsplash.model.search

import org.bubbble.andsplash.model.photo.CollectionsInfo

/**
 * @author Andrew
 * @date 2020/12/22 20:17
 */
data class SearchCollectionsInfo(
    val total: Long,
    val total_pages: Long,
    val results: List<CollectionsInfo>
)

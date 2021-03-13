package org.bubbble.andsplash.model.photo

/**
 * @author Andrew
 * @date 2020/12/21 15:39
 */
data class LocationInfo(
    val city: String,
    val country: String,
    val position: Position
)

data class Position(
    val latitude: String,
    val longitude: String
)
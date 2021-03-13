package org.bubbble.andsplash.model.photo

/**
 * @author Andrew
 * @date 2020/12/21 15:38
 */
data class ExifInfo(
    val make: String,
    val model: String,
    val exposure_time: String,
    val aperture: String,
    val focal_length: String,
    val iso: String,
)

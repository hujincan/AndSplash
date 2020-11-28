package org.bubbble.andsplash.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * @author Andrew
 * @date 2020/10/20 13:53
 */
object CircularOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(
            view.paddingLeft,
            view.paddingTop,
            view.width - view.paddingRight,
            view.height - view.paddingBottom
        )
    }
}

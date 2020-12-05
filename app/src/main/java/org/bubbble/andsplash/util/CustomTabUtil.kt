package org.bubbble.andsplash.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.bubbble.andsplash.shared.data.ConnectionURL

/**
 * @author Andrew
 * @date 2020/12/05 16:46
 */
object CustomTabUtil {

    fun open(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}
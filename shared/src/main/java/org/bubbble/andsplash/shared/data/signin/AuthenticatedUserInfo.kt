package org.bubbble.andsplash.shared.data.signin

import android.net.Uri

/**
 * @author Andrew
 * @date 2020/10/25 18:45
 */
interface AuthenticatedUserInfo {

    fun isSignedIn(): Boolean

    fun getEmail(): String?

    fun getDisplayName(): String?

    fun getPhotoUrl(): String?

}

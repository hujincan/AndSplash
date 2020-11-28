package org.bubbble.andsplash.shared.domain.auth

import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.shared.data.signin.UnsplashAccount

/**
 * @author Andrew
 * @date 2020/10/25 20:22
 */
class GoogleAuthUserInfo(private val account: UnsplashAccount?) : AuthenticatedUserInfo {

    override fun isSignedIn(): Boolean = account != null

    override fun getEmail(): String? = account?.email

    override fun getDisplayName(): String? = account?.displayName

    override fun getPhotoUrl(): String? = account?.photoUrl
}
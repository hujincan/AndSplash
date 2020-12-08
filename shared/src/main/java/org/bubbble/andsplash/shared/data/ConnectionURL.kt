package org.bubbble.andsplash.shared.data


/**
 * @author Andrew
 * @date 2020/12/05 15:10
 */

object ConnectionURL {

    const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
    const val UNSPLASH_JOIN_URL = "https://unsplash.com/join"
    const val UNSPLASH_URL = "https://unsplash.com/"
    const val UNSPLASH_LOGIN_CALLBACK = "unsplash-auth-callback"

    const val LOGIN_URL = UNSPLASH_URL + "oauth/authorize" +
            "?client_id=" + BuildConfig.ACCESS_KEY +
            "&redirect_uri=" + "andsplash%3A%2F%2F" + UNSPLASH_LOGIN_CALLBACK +
            "&response_type=" + "code" +
            "&scope=" + "public+read_user+write_user+read_photos+write_photos+write_likes" +
            "+write_followers+read_collections+write_collections"
}

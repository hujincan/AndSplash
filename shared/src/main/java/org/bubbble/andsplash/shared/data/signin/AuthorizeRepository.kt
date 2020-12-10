package org.bubbble.andsplash.shared.data.signin

import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.network.service.AuthorizeService
import org.bubbble.andsplash.shared.util.PreferencesUtil
import retrofit2.Response

/**
 * @author Andrew
 * @date 2020/12/05 21:51
 */
interface AuthorizeRepository {
    suspend fun getAccessToken(code: String?): Boolean
}

class DefaultAuthorizeRepository(
    private val service: AuthorizeService,
    private val preferencesUtil: PreferencesUtil): AuthorizeRepository {

    override suspend fun getAccessToken(code: String?): Boolean {
        val result = service.requestAccessToken(code)
        return if (result.isSuccessful) {
            result.body()?.let {
                saveAccessToken(it)
            }
            true
        } else {
            false
        }
    }

    private fun saveAccessToken(accessToken: AccessToken) {
        preferencesUtil.run {
            put(BuildConfig.ACCESS_TOKEN, accessToken.access_token)
            put(BuildConfig.TOKEN_TYPE, accessToken.token_type)
            put(BuildConfig.SCOPE, accessToken.scope)
            put(BuildConfig.CREATED_AT, accessToken.created_at)
        }
    }
}
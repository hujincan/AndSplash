package org.bubbble.andsplash.shared.data.signin

import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.network.service.AuthorizeService
import retrofit2.Response

/**
 * @author Andrew
 * @date 2020/12/05 21:51
 */
interface AuthorizeRepository {

    suspend fun getAccessToken(code: String?): Response<AccessToken>
}

class DefaultAuthorizeRepository(private val service: AuthorizeService): AuthorizeRepository {

    override suspend fun getAccessToken(code: String?): Response<AccessToken> {
        return service.requestAccessToken(code)
    }

}
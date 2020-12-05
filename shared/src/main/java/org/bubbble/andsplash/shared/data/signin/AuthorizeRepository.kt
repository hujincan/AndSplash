package org.bubbble.andsplash.shared.data.signin

import androidx.lifecycle.MutableLiveData
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.network.service.AuthorizeService

/**
 * @author Andrew
 * @date 2020/12/05 21:51
 */
interface AuthorizeRepository {

    suspend fun getAccessToken(code: String?): AccessToken
}

class DefaultAuthorizeRepository(private val service: AuthorizeService): AuthorizeRepository {

    override suspend fun getAccessToken(code: String?): AccessToken {
        return service.requestAccessToken(code)
    }

}
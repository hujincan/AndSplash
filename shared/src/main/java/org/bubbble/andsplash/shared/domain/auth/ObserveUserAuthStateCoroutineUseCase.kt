package org.bubbble.andsplash.shared.domain.auth


import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.signin.AuthorizeRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase

/**
 * @author Andrew
 * @date 2020/10/25 19:29
 */

class ObserveUserAuthStateCoroutineUseCase(
    private val repository: AuthorizeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String?, AccessToken>(dispatcher) {

    override suspend fun execute(parameters: String?): AccessToken {
        return repository.getAccessToken(parameters)
    }
}
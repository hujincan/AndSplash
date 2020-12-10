package org.bubbble.andsplash.shared.domain.auth


import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.signin.AuthorizeRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/10/25 19:29
 */

class UserAuthSaveUseCase @Inject constructor (
    private val repository: AuthorizeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String?, Boolean>(dispatcher) {

    override suspend fun execute(parameters: String?): Boolean {
        return repository.getAccessToken(parameters)
    }
}
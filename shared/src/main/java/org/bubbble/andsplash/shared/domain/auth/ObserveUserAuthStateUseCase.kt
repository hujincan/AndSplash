package org.bubbble.andsplash.shared.domain.auth


import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.shared.data.signin.UnsplashAccount
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.UseCase

/**
 * @author Andrew
 * @date 2020/10/25 19:29
 */

class ObserveUserAuthStateUseCase(
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<UnsplashAccount?, AuthenticatedUserInfo>(dispatcher) {

    override suspend fun execute(parameters: UnsplashAccount?): AuthenticatedUserInfo {
        return GoogleAuthUserInfo(parameters)
    }

}
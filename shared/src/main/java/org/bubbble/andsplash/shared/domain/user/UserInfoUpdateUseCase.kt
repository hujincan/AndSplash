package org.bubbble.andsplash.shared.domain.user

import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.model.user.UserInfo
import org.bubbble.andsplash.shared.data.user.UserDataRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/08 15:03
 */
class UserInfoUpdateUseCase @Inject constructor(
    private val repository: UserDataRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, UserInfo?>(dispatcher) {

    override suspend fun execute(parameters: Unit): UserInfo? {
        return repository.getUserInfo()
    }
}
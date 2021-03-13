package org.bubbble.andsplash.shared.domain.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.data.user.UserDataRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.MediatorUseCase
import org.bubbble.andsplash.shared.result.Result
import org.bubbble.andsplash.shared.util.logger
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/08 15:03
 */
class UserInfoUpdateUseCase @Inject constructor(
    private val repository: UserDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MediatorUseCase<CoroutineScope, UserEntity>() {

    override fun execute(parameters: CoroutineScope) {
        try {
            result.removeSource(repository.observerResult)
            result.addSource(repository.observerResult) {
                result.value = Result.Success(it)
            }

            parameters.launch {
                repository.getUserInfo()
            }
        } catch (e: Exception) {
            logger(e.message)
            result.value = Result.Error("${e.message}")
        }
    }

    suspend fun removeAccount(currentUserId: Int) {
        withContext(dispatcher) {
            repository.removeUserInfo(currentUserId)
        }
    }

    suspend fun signOutAccount(currentUserId: Int) {
        withContext(dispatcher) {
            repository.signOutUserInfo(currentUserId)
        }
    }

    suspend fun switchAccount(currentUserId: Int, accessToken: String) {
        withContext(dispatcher) {
            repository.switchUserInfo(currentUserId, accessToken)
        }
    }
}
package org.bubbble.andsplash.shared.domain.user

import kotlinx.coroutines.CoroutineDispatcher
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
) : MediatorUseCase<Unit, UserEntity>() {

    override suspend fun execute(parameters: Unit): UserEntity {
        try {
            val request = repository.getUserInfo()
            result.addSource(request) {
                result.postValue(Result.Success(it))
            }
            return request.value ?: UserEntity.getDefault()
        } catch (e: Exception) {
            result.postValue(Result.Error("${e.message}"))
        }

        return UserEntity.getDefault()
    }

    suspend fun signOut(currentUserId: Int) {
        withContext(dispatcher) {
            repository.removeUserInfo(currentUserId)
        }
    }

    suspend fun switchAccount(currentUserId: Int, accessToken: String) {
        withContext(dispatcher) {
            repository.switchUserInfo(currentUserId, accessToken)
        }
    }
}
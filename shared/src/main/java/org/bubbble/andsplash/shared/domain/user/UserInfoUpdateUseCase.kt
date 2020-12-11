package org.bubbble.andsplash.shared.domain.user

import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.data.user.UserDataRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.MediatorUseCase
import org.bubbble.andsplash.shared.result.Result
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/08 15:03
 */
class UserInfoUpdateUseCase @Inject constructor(
    private val repository: UserDataRepository
) : MediatorUseCase<Unit, UserEntity?>() {

    override suspend fun execute(parameters: Unit) {
        try {
            val request = repository.getUserInfo()
            result.removeSource(request)
            result.addSource(request) {
                result.postValue(Result.Success(it))
            }
        } catch (e: Exception) {
            result.postValue(Result.Error("${e.message}"))
        }
    }
}
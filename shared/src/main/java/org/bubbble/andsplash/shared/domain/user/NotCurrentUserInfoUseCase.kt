package org.bubbble.andsplash.shared.domain.user

import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/11 10:31
 */
class NotCurrentUserInfoUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val appDatabase: AppDatabase
) : CoroutineUseCase<Int, List<UserEntity>>(dispatcher) {
    override suspend fun execute(parameters: Int): List<UserEntity> {
        return appDatabase.userDao().getNotCurrentAll(parameters)
    }
}
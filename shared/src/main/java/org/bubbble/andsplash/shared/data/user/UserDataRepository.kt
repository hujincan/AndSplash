package org.bubbble.andsplash.shared.data.user

import org.bubbble.andsplash.model.user.UserInfo
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.network.service.UserInfoService
import org.bubbble.andsplash.shared.util.logger

/**
 * @author Andrew
 * @date 2020/12/08 15:09
 */
interface UserDataRepository {
    suspend fun getUserInfo(): UserInfo?
}

class DefaultUserDataRepository(
    private val service: UserInfoService,
    private val appDatabase: AppDatabase): UserDataRepository {

    override suspend fun getUserInfo(): UserInfo? {
        val request = service.requestUserInfo()
        if (request.isSuccessful) {
            request.body()?.let {
                saveUserInfo(it)
            }
        }
        return request.body()
    }

    private suspend fun saveUserInfo(userInfo: UserInfo) {

        logger("$userInfo")
        appDatabase.userDao().saveAll(UserEntity(
            userInfo.numeric_id,
            userInfo.username,
            userInfo.name,
            userInfo.first_name,
            userInfo.last_name,
            userInfo.location,
            userInfo.profile_image.large,
            userInfo.instagram_username,
            userInfo.total_collections,
            userInfo.total_likes,
            userInfo.total_photos,
            userInfo.email,
        ))
    }
}
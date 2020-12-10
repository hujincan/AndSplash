package org.bubbble.andsplash.shared.data.user

import org.bubbble.andsplash.model.user.UserInfo
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.network.service.UserInfoService
import org.bubbble.andsplash.shared.util.PreferencesUtil
import org.bubbble.andsplash.shared.util.logger

/**
 * @author Andrew
 * @date 2020/12/08 15:09
 */
interface UserDataRepository {
    suspend fun getUserInfo(): UserEntity?
}

class DefaultUserDataRepository(
    private val service: UserInfoService,
    private val preferencesUtil: PreferencesUtil,
    private val appDatabase: AppDatabase): UserDataRepository {

    override suspend fun getUserInfo(): UserEntity? {

        val userRequest = appDatabase.userDao().getCurrentUser(preferencesUtil.get(BuildConfig.CURRENT_USER_ID, -1))
        if (userRequest.isNotEmpty()) {
            return userRequest[0]
        }

        if (preferencesUtil.get(BuildConfig.ACCESS_TOKEN, "").isNotEmpty()) {
            val request = service.requestUserInfo()
            return if (request.isSuccessful) {
                request.body()?.let {
                    saveUserInfo(it)
                }
            } else {
                null
            }
        }

        return null
    }

    private suspend fun saveUserInfo(userInfo: UserInfo): UserEntity {
        preferencesUtil.put(BuildConfig.CURRENT_USER_ID, userInfo.numeric_id)
        val data = UserEntity(
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
        )
        appDatabase.userDao().setAll(data)
        return data
    }
}
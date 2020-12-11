package org.bubbble.andsplash.shared.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.model.user.UserInfo
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.network.service.UserInfoService
import org.bubbble.andsplash.shared.util.PreferencesUtil
import org.bubbble.andsplash.shared.util.logger

/**
 * @author Andrew
 * @date 2020/12/08 15:09
 */
interface UserDataRepository {
    suspend fun getUserInfo(): LiveData<UserEntity?>
}

class DefaultUserDataRepository(
    private val service: UserInfoService,
    private val preferencesUtil: PreferencesUtil,
    private val appDatabase: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): UserDataRepository {

    override suspend fun getUserInfo(): LiveData<UserEntity?> {

        val result: MutableLiveData<UserEntity> by lazy {
            MutableLiveData<UserEntity>()
        }

        withContext(dispatcher) {
            val userRequest = appDatabase.userDao().getCurrentUser(preferencesUtil.get(BuildConfig.CURRENT_USER_ID, -1))

            // 从数据库读取
            if (userRequest.isNotEmpty()) {
                result.postValue(userRequest[0])
            }

            // 从网络获取并更新到数据库
            val token = preferencesUtil.get(BuildConfig.ACCESS_TOKEN, "")
            if (token.isNotEmpty()) {
                val request = service.requestUserInfo()
                if (request.isSuccessful) {
                    request.body()?.let {
                        result.postValue(saveUserInfo(it, token, userRequest))
                    }
                }
            }
        }
        return result
    }

    private suspend fun saveUserInfo(userInfo: UserInfo, token: String, userRequest: List<UserEntity>): UserEntity {
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
            token
        )

        if (userRequest.isEmpty()) {
            // 数据库内不存在 -> 保存到数据库
            appDatabase.userDao().updateCurrentUser(data)
            appDatabase.userDao().setAll(data)
        } else if (userRequest.isNotEmpty() && userRequest[0] != data) {
            // 数据库内存在并且内容不相等 -> 更新到数据库
            appDatabase.userDao().updateCurrentUser(data)
        }

        return data
    }
}
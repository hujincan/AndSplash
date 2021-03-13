package org.bubbble.andsplash.shared.data.user

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.model.user.MeInfo
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
    suspend fun getUserInfo()
    suspend fun removeUserInfo(currentUserId: Int)
    suspend fun signOutUserInfo(currentUserId: Int?)
    suspend fun switchUserInfo(currentUserId: Int, accessToken: String)

    val observerResult: MutableLiveData<UserEntity>

}

class DefaultUserDataRepository(
    private val service: UserInfoService,
    private val preferencesUtil: PreferencesUtil,
    private val appDatabase: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): UserDataRepository {

    val result: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }

    override suspend fun getUserInfo() {

        withContext(dispatcher) {
            val id = preferencesUtil.get(BuildConfig.CURRENT_USER_ID, -1)
            logger("当前用户id： $id")
            val userRequest = appDatabase.userDao().getCurrentUser(id)

            // 从数据库读取
            if (userRequest.isNotEmpty()) {
                result.postValue(userRequest[0])
            } else {
                result.postValue(UserEntity.getDefault())
            }

            // 从网络获取并更新到数据库
            val token = preferencesUtil.get(BuildConfig.ACCESS_TOKEN, "")
            if (token.isNotEmpty()) {
                val request = service.requestUserInfo()
                if (request.isSuccessful) {
                    request.body()?.let {
                        result.postValue(saveUserInfo(it, token, userRequest, id))
                    }
                }
            }
        }
    }

    override suspend fun removeUserInfo(currentUserId: Int) {
        appDatabase.userDao().removeUserById(currentUserId)
    }

    private suspend fun saveUserInfo(meInfo: MeInfo, token: String, userRequest: List<UserEntity>, id: Int): UserEntity {
        preferencesUtil.put(BuildConfig.CURRENT_USER_ID, meInfo.numeric_id)
        val data = UserEntity(
            meInfo.numeric_id,
            meInfo.username,
            meInfo.name,
            meInfo.first_name,
            meInfo.last_name,
            meInfo.location,
            meInfo.profile_image.large,
            meInfo.instagram_username,
            meInfo.portfolio_url,
            meInfo.bio,
            meInfo.total_collections,
            meInfo.total_likes,
            meInfo.total_photos,
            meInfo.email,
            meInfo.downloads,
            token
        )

        if (userRequest.isEmpty() || id != meInfo.numeric_id) {
            // 数据库内不存在 -> 保存到数据库
            appDatabase.userDao().updateCurrentUser(data)
            appDatabase.userDao().setAll(data)
        } else if (userRequest.isNotEmpty() && userRequest[0] != data) {
            // 数据库内存在并且内容不相等 -> 更新到数据库
            appDatabase.userDao().updateCurrentUser(data)
        }

        return data
    }

    override suspend fun signOutUserInfo(currentUserId: Int?) {
        currentUserId?.let {
            appDatabase.userDao().removeUserById(it)
        }
        preferencesUtil.run {
            remove(BuildConfig.CURRENT_USER_ID)
            remove(BuildConfig.ACCESS_TOKEN)
            remove(BuildConfig.TOKEN_TYPE)
            remove(BuildConfig.SCOPE)
            remove(BuildConfig.CREATED_AT)
        }
    }

    override suspend fun switchUserInfo(currentUserId: Int, accessToken: String) {
        preferencesUtil.run {
            remove(BuildConfig.CURRENT_USER_ID)
            remove(BuildConfig.ACCESS_TOKEN)
            remove(BuildConfig.TOKEN_TYPE)
            remove(BuildConfig.SCOPE)
            remove(BuildConfig.CREATED_AT)

            put(BuildConfig.CURRENT_USER_ID, currentUserId)
            put(BuildConfig.ACCESS_TOKEN, accessToken)
        }
    }

    override val observerResult: MutableLiveData<UserEntity>
        get() = result
}
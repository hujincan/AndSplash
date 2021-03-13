package org.bubbble.andsplash.shared.data.photo

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.model.photo.PhotoInfo
import org.bubbble.andsplash.model.photo.UrlsInfo
import org.bubbble.andsplash.model.user.ProfileImage
import org.bubbble.andsplash.model.user.UserInfo
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.network.service.PhotoService

/**
 * @author Andrew
 * @date 2021/02/06 16:54
 */
interface PhotoRepository {

    suspend fun getAllPhoto(
        page: Int,
        per_page: Int,
        order_by: String
    )


    val observerResult: MutableLiveData<List<PhotoInfo>>
}

class DefaultPhotoRepository(
    private val service: PhotoService,
    private val appDatabase: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): PhotoRepository {

    val result: MutableLiveData<List<PhotoInfo>> by lazy {
        MutableLiveData<List<PhotoInfo>>()
    }

    override suspend fun getAllPhoto(
        page: Int,
        per_page: Int,
        order_by: String
    ) {
        withContext(dispatcher) {

            // 从数据库中读取
            val localData = getLocalAllPhoto(page, per_page)
            if (localData.isNotEmpty()) {
                result.postValue(localData)
            } else {
                // 什么数据都没有
            }

            // 从网络获取并更新到数据库
            val request = service.getAllPhoto(page, per_page, order_by)
            if (request.isSuccessful) {
                request.body()?.let {
                    result.postValue(it)
                    saveAllPhoto(page, it)
                }
            }
        }
    }

    private suspend fun saveAllPhoto(page: Int, photos: List<PhotoInfo>) {
        if (page == 1) {
            appDatabase.photoDao().clearAllPhoto()
        }
    }

    private suspend fun getLocalAllPhoto(page: Int, per_page: Int): List<PhotoInfo> {
        val data = appDatabase.photoDao().getAllPhoto(per_page, (page - 1) * per_page)

        val list = ArrayList<PhotoInfo>()
        for (value in data) {
            list.add(
                PhotoInfo(
                    value.id,
                    value.created_at,
                    value.updated_at,
                    value.width,
                    value.height,
                    value.color,
                    value.downloads,
                    value.likes,
                    value.liked_by_user,
                    value.description,
                    UserInfo(
                        null,
                        value.user_name,
                        null,
                        null,
                        null,
                        ProfileImage(
                            value.user_profile,
                            null,
                            null),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                    UrlsInfo(value.user_profile, null, null, null, null),
                    null,
                    null,
                    null,
                    null,
            ))
        }
        return list
    }

    override val observerResult: MutableLiveData<List<PhotoInfo>>
        get() = result

}
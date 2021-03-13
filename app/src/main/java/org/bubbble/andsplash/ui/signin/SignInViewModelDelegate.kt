package org.bubbble.andsplash.ui.signin

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.domain.auth.UserAuthSaveUseCase
import org.bubbble.andsplash.shared.domain.user.UserInfoUpdateUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.result.Result
import org.bubbble.andsplash.shared.result.data
import org.bubbble.andsplash.shared.util.logger
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/10/25 10:56
 */

enum class SignInEvent {
    RequestSignIn, RequestSignOut
}

interface SignInViewModelDelegate {

    val currentUserInfo: LiveData<UserEntity>

    fun isSignedIn(): Boolean

    suspend fun setUserInfo(code: String, viewModelScope: CoroutineScope)

    fun updateUserInfo(viewModelScope: CoroutineScope)

    fun onSignIn()

    suspend fun onSignOut(viewModelScope: CoroutineScope)

    fun observeSignedInUser(): LiveData<Boolean>

    val performSignInEvent: MutableLiveData<Event<SignInEvent>>

    suspend fun switchUserInfo(currentUserId: Int, accessToken: String, viewModelScope: CoroutineScope)

    suspend fun removeUserInfo(currentUserId: Int, viewModelScope: CoroutineScope)
}

/**
 * 获取最新的用户数据，如果没有网络则不更新，使用本地已存储数据。
 */
internal class UnsplashSignInViewModelDelegate @Inject constructor(
    private val userAuthSaveUseCase: UserAuthSaveUseCase,
    private val userInfoUpdateUseCase: UserInfoUpdateUseCase,
    @ApplicationContext val context: Context
) : SignInViewModelDelegate {

    private val _currentUserInfo: LiveData<UserEntity>
    override val currentUserInfo: LiveData<UserEntity>
        get() = _currentUserInfo

    init {
        _currentUserInfo = userInfoUpdateUseCase.observe().map {
            val result = (it as? Result.Success)?.data ?: UserEntity.getDefault()
            _currentIsSignedIn.value = result.numeric_id != null
            result
        }
    }

    override suspend fun setUserInfo(code: String, viewModelScope: CoroutineScope) {
        // 如果code不是null 并且 正确拿到token。则更新用户数据
        if (userAuthSaveUseCase(code).data == false) {
            _currentIsSignedIn.value = false
        } else {
            logger("有执行")
            updateUserInfo(viewModelScope)
        }
    }

    override fun updateUserInfo(viewModelScope: CoroutineScope) {
        userInfoUpdateUseCase(viewModelScope)
    }

    override val performSignInEvent = MutableLiveData<Event<SignInEvent>>()

    override suspend fun switchUserInfo(currentUserId: Int, accessToken: String, viewModelScope: CoroutineScope) {
        userInfoUpdateUseCase.switchAccount(currentUserId, accessToken)
        updateUserInfo(viewModelScope)
    }

    override suspend fun removeUserInfo(
        currentUserId: Int,
        viewModelScope: CoroutineScope
    ) {
        userInfoUpdateUseCase.removeAccount(currentUserId)
        updateUserInfo(viewModelScope)
    }

    override fun onSignIn() {
        performSignInEvent.value = Event(SignInEvent.RequestSignIn)
    }

    override suspend fun onSignOut(viewModelScope: CoroutineScope) {
        currentUserInfo.value?.let {
            it.numeric_id?.let { numeric_id ->
                performSignInEvent.value = Event(SignInEvent.RequestSignOut)
                userInfoUpdateUseCase.signOutAccount(numeric_id)
                _currentIsSignedIn.value = false
                updateUserInfo(viewModelScope)
            }
        }
    }

    private val _currentIsSignedIn = MutableLiveData<Boolean>()

    private val isSignedIn: LiveData<Boolean>
        get() = _currentIsSignedIn

    override fun isSignedIn(): Boolean = isSignedIn.value == true

    override fun observeSignedInUser(): LiveData<Boolean> = isSignedIn
}
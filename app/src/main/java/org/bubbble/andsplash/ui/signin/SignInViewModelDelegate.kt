package org.bubbble.andsplash.ui.signin

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.domain.auth.UserAuthSaveUseCase
import org.bubbble.andsplash.shared.domain.user.UserInfoUpdateUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.result.data
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

    val currentUserId: LiveData<Int>

    val currentUserImageUri: LiveData<String?>

    fun isSignedIn(): Boolean

    suspend fun handleSignInResult(code: String?)

    fun onSignIn()

    fun onSignOut()

    fun observeSignedInUser(): LiveData<Boolean>

    val performSignInEvent: MutableLiveData<Event<SignInEvent>>
}

/**
 * 获取最新的用户数据，如果没有网络则不更新，使用本地已存储数据。
 */
internal class UnsplashSignInViewModelDelegate @Inject constructor(
    private val userAuthSaveUseCase: UserAuthSaveUseCase,
    private val userInfoUpdateUseCase: UserInfoUpdateUseCase,
    @ApplicationContext val context: Context
) : SignInViewModelDelegate {

    private val _currentUserInfo = MutableLiveData<UserEntity>()
    override val currentUserInfo: LiveData<UserEntity>
        get() = _currentUserInfo

    private val _currentUserId = MutableLiveData<Int>()
    override val currentUserId: LiveData<Int>
        get() = _currentUserId

    private val _currentUserImageUri = MutableLiveData<String?>()
    override val currentUserImageUri: LiveData<String?>
        get() = _currentUserImageUri

    override suspend fun handleSignInResult(code: String?) {
        // 如果code不是null 并且 正确拿到token。则更新用户数据
        if (code != null && userAuthSaveUseCase(code).data != true) {
            return
        }
        updateUserInfo()
    }

    private suspend fun updateUserInfo() {
        userInfoUpdateUseCase(Unit).map { result ->
            result.data?.let {
                _currentUserInfo.value = it
                _currentUserImageUri.value = it.profile_image
                _currentIsSignedIn.value = true
                _currentUserId.value = it.numeric_id
            }
        }
    }

    override val performSignInEvent = MutableLiveData<Event<SignInEvent>>()

    override fun onSignIn() {
        performSignInEvent.value = Event(SignInEvent.RequestSignIn)
    }

    override fun onSignOut() {
        performSignInEvent.value = Event(SignInEvent.RequestSignOut)
    }

    private val _currentIsSignedIn = MutableLiveData<Boolean>()
    private val isSignedIn: LiveData<Boolean>
        get() = _currentIsSignedIn

    override fun isSignedIn(): Boolean = isSignedIn.value == true

    override fun observeSignedInUser(): LiveData<Boolean> = isSignedIn
}
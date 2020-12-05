package org.bubbble.andsplash.ui.signin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.shared.domain.auth.ObserveUserAuthStateCoroutineUseCase
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

    val currentUserInfo: LiveData<AuthenticatedUserInfo?>

    val resultTest: LiveData<String?>

    /**
     * Live updated value of the current firebase users image url
     */
    val currentUserImageUri: LiveData<Uri?>

    fun isSignedIn(): Boolean

    suspend fun handleSignInResult(data: String?)

    fun onSignIn()

    fun onSignOut()

    fun observeSignedInUser(): LiveData<Boolean>

    val performSignInEvent: MutableLiveData<Event<SignInEvent>>
}

internal class GoogleSignInViewModelDelegate @Inject constructor(
    private val observerUserAuthStateUseCase: ObserveUserAuthStateCoroutineUseCase,
    @ApplicationContext val context: Context
) : SignInViewModelDelegate {

    private val _currentUserInfo = MutableLiveData<AuthenticatedUserInfo?>()
    override val currentUserInfo: LiveData<AuthenticatedUserInfo?>
        get() = _currentUserInfo

    private val _currentUserImageUri = MutableLiveData<Uri?>()
    override val currentUserImageUri: LiveData<Uri?>
        get() = _currentUserImageUri


    private val _resultTest = MutableLiveData<String?>()
    override val resultTest: LiveData<String?>
        get() = _resultTest

    override suspend fun handleSignInResult(data: String?) {
        val fine = observerUserAuthStateUseCase(data).data
        _resultTest.value = "access_token：${fine?.access_token} \n token_type：${fine?.token_type} \n scope：${fine?.scope} \n created_at：${fine?.created_at}"
    }

    override val performSignInEvent = MutableLiveData<Event<SignInEvent>>()

    override fun onSignIn() {
        Log.e("AND", "onSignIn")
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
package org.bubbble.andsplash.ui.signin

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import org.bubbble.andsplash.shared.data.signin.AuthenticatedUserInfo
import org.bubbble.andsplash.shared.domain.auth.ObserveUserAuthStateUseCase
import org.bubbble.andsplash.shared.result.Event
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

    val currentUserInfo: LiveData<AuthenticatedUserInfo?>

    /**
     * Live updated value of the current firebase users image url
     */
    val currentUserImageUri: LiveData<Uri?>

    fun isSignedIn(): Boolean

    suspend fun handleSignInResult(data: Intent?)

    fun onSignIn()

    fun onSignOut()

    fun observeSignedInUser(): LiveData<Boolean>

    val performSignInEvent: MutableLiveData<Event<SignInEvent>>
}


internal class GoogleSignInViewModelDelegate @Inject constructor(
    private val observerUserAuthStateUseCase: ObserveUserAuthStateUseCase,
    @ApplicationContext val context: Context
) : SignInViewModelDelegate {

    private val _currentUserInfo = MutableLiveData<AuthenticatedUserInfo?>()
    override val currentUserInfo: LiveData<AuthenticatedUserInfo?>
        get() = _currentUserInfo

    private val _currentUserImageUri = MutableLiveData<Uri?>()
    override val currentUserImageUri: LiveData<Uri?>
        get() = _currentUserImageUri

    override suspend fun handleSignInResult(data: Intent?) {

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
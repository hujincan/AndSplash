package org.bubbble.andsplash.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.ui.signin.SignInViewModelDelegate

/**
 * @author Andrew
 * @date 2020/10/21 20:07
 */
class MainActivityViewModel @ViewModelInject constructor(
    signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate{

    private val _navigateToSignInDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToSignInDialogAction: LiveData<Event<Unit>>
        get() = _navigateToSignInDialogAction

    private val _navigateToSignOutDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToSignOutDialogAction: LiveData<Event<Unit>>
        get() = _navigateToSignOutDialogAction

    init {
        updateUserInfo(viewModelScope)
    }

    fun onProfileClicked() {
        if (isSignedIn()) {
            _navigateToSignOutDialogAction.value = Event(Unit)
        } else {
            _navigateToSignInDialogAction.value = Event(Unit)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            onSignOut(viewModelScope)
        }
    }

    fun switchUser(currentUserId: Int, accessToken: String) {
        viewModelScope.launch {
            switchUserInfo(currentUserId, accessToken, viewModelScope)
        }
    }

    fun removeUser(currentUserId: Int) {
        viewModelScope.launch {
            removeUserInfo(currentUserId, viewModelScope)
        }
    }

    fun signIn(code: String) {
        viewModelScope.launch {
            setUserInfo(code, viewModelScope)
        }
    }
}
package org.bubbble.andsplash.ui.hello

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bubbble.andsplash.shared.domain.prefs.HelloCompleteActionUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.ui.signin.SignInViewModelDelegate

/**
 * @author Andrew
 * @date 2020/10/20 11:44
 */
class HelloViewModel @ViewModelInject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    private val helloCompleteActionUseCase: HelloCompleteActionUseCase
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    private val _navigateToSignInDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToSignInDialogAction: LiveData<Event<Unit>> = _navigateToSignInDialogAction

    fun getStartedClick() {
        viewModelScope.launch { helloCompleteActionUseCase(true) }
        _navigateToMainActivity.value = Event(Unit)
    }

    fun onSigninClicked() {
        viewModelScope.launch { helloCompleteActionUseCase(true) }
        _navigateToSignInDialogAction.value = Event(Unit)
    }
}
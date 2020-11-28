package org.bubbble.andsplash.ui.hello

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.util.PreferencesUtil
import org.bubbble.andsplash.ui.signin.SignInViewModelDelegate

/**
 * @author Andrew
 * @date 2020/10/20 11:44
 */
class HelloViewModel @ViewModelInject constructor(
    @ActivityContext private val context: Context,
    signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    private val _navigateToSignInDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToSignInDialogAction: LiveData<Event<Unit>> = _navigateToSignInDialogAction

    fun getStartedClick() {
        viewModelScope.launch {
            PreferencesUtil.put(context, "skip", true)
            _navigateToMainActivity.postValue(Event(Unit))
        }
    }

    fun onSigninClicked() {
        _navigateToSignInDialogAction.value = Event(Unit)
    }
}
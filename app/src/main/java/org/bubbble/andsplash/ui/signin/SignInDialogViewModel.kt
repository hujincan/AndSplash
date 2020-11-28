package org.bubbble.andsplash.ui.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import org.bubbble.andsplash.shared.result.Event

class SignInDialogViewModel @ViewModelInject constructor(
    signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _dismissDialogAction = MutableLiveData<Event<Unit>>()
    val dismissDialogAction: LiveData<Event<Unit>>
        get() = _dismissDialogAction

    fun onCancel() {
        _dismissDialogAction.value = Event(Unit)
    }
}
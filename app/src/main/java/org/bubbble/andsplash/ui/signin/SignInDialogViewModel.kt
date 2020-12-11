package org.bubbble.andsplash.ui.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.domain.user.NotCurrentUserInfoUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.result.data

class SignInDialogViewModel @ViewModelInject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    private val notCurrentUserInfoUseCase: NotCurrentUserInfoUseCase
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _dismissDialogAction = MutableLiveData<Event<Unit>>()
    val dismissDialogAction: LiveData<Event<Unit>>
        get() = _dismissDialogAction

    fun onCancel() {
        _dismissDialogAction.value = Event(Unit)
    }

    var hasOtherAccount = false

    private val observer = Observer<Int> {
        viewModelScope.launch {
            currentUserId.value?.let { id ->
                notCurrentUserInfoUseCase(id).data?.let {
                    notCurrentAccounts.value = it
                    hasOtherAccount = true
                } ?: run {
                    hasOtherAccount = false
                }
            }
        }
    }

    init {
        currentUserId.observeForever(observer)
    }

    val notCurrentAccounts: MutableLiveData<List<UserEntity>> by lazy {
        MutableLiveData<List<UserEntity>>()
    }

    override fun onCleared() {
        currentUserId.removeObserver(observer)
        super.onCleared()
    }
}
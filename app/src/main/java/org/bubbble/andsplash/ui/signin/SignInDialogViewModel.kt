package org.bubbble.andsplash.ui.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.bubbble.andsplash.shared.data.db.UserEntity
import org.bubbble.andsplash.shared.domain.user.NotCurrentUserInfoUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.result.data
import org.bubbble.andsplash.shared.util.logger

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

    val hasOtherAccount = MutableLiveData<Boolean>()

    private val observer = Observer<UserEntity> {
        viewModelScope.launch {
            logger(it.numeric_id.toString())
            notCurrentUserInfoUseCase(it.numeric_id ?: 0).data?.let {
                if (it.isNotEmpty()) {
                    notCurrentAccounts.value = it
                    logger("有其他帐号")
                    hasOtherAccount.value = true
                } else {
                    logger("没有其他帐号")
                    hasOtherAccount.value = false
                }
            } ?: run {
                hasOtherAccount.value = false
                logger("没有其他帐号")
            }
        }
    }

    init {
        currentUserInfo.observeForever(observer)
        logger("currentUserInfo ： ${currentUserInfo.value}")
    }

    val notCurrentAccounts: MutableLiveData<List<UserEntity>> by lazy {
        MutableLiveData<List<UserEntity>>()
    }

    override fun onCleared() {
        currentUserInfo.removeObserver(observer)
        super.onCleared()
    }
}
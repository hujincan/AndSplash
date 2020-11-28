package org.bubbble.andsplash.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.bubbble.andsplash.shared.domain.prefs.OnLaunchUseCase
import org.bubbble.andsplash.shared.result.Event
import org.bubbble.andsplash.shared.result.data

/**
 * @author Andrew
 * @date 2020/10/20 11:22
 */
class LaunchViewModel @ViewModelInject constructor(
    onLaunchUseCase: OnLaunchUseCase
) : ViewModel() {
    val launchDestination = liveData {
        val result = onLaunchUseCase(Unit)
        if (result.data == false) {
            emit(Event(LaunchDestination.HELLO_ACTIVITY))
        } else {
            emit(Event(LaunchDestination.MAIN_ACTIVITY))
        }
    }
}

enum class LaunchDestination {
    HELLO_ACTIVITY,
    MAIN_ACTIVITY
}
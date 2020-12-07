package org.bubbble.andsplash.shared.domain.prefs

import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import org.bubbble.andsplash.shared.util.PreferencesUtil
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/07 20:42
 */
class HelloCompleteActionUseCase @Inject constructor(
    private val preferencesUtil: PreferencesUtil,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Boolean, Unit>(dispatcher) {

    override suspend fun execute(parameters: Boolean) {
        preferencesUtil.put(BuildConfig.SKIP, parameters)
    }
}
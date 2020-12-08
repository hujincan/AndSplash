package org.bubbble.andsplash.shared.domain.prefs

import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.model.AccessToken
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import org.bubbble.andsplash.shared.util.PreferencesUtil
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/12/07 20:17
 */
class AccessTokenUseCase @Inject constructor(
    private val preferencesUtil: PreferencesUtil,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, String?>(dispatcher) {

    override suspend fun execute(parameters: Unit): String? {
        return preferencesUtil.get(BuildConfig.ACCESS_TOKEN, null)
    }
}
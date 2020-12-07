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
class AccessTokenSaveUseCase @Inject constructor(
    private val preferencesUtil: PreferencesUtil,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<AccessToken, Unit>(dispatcher) {
    override suspend fun execute(parameters: AccessToken) {
        preferencesUtil.run {
            put(BuildConfig.ACCESS_TOKEN, parameters.access_token)
            put(BuildConfig.TOKEN_TYPE, parameters.token_type)
            put(BuildConfig.SCOPE, parameters.scope)
            put(BuildConfig.CREATED_AT, parameters.created_at)
        }
    }
}
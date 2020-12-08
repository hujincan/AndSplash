package org.bubbble.andsplash.shared.domain.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import org.bubbble.andsplash.shared.data.db.AppDatabase
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.CoroutineUseCase
import org.bubbble.andsplash.shared.util.PreferencesUtil
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2020/10/19 9:30
 */
class OnLaunchCoroutineUseCase @Inject constructor(
    private val preferencesUtil: PreferencesUtil,
    // 看到这里的 @IoDispatcher 了吗，这里其实最终依赖的实例是 Dispatchers.IO
    @IoDispatcher dispatcher: CoroutineDispatcher

    // 这里的泛型<Unit, Boolean>，对应<P, R>，P是参数，R是返回值，
    // 这里是继承了UseCase，并且整个类只有一个功能，所以现在就可以明确泛型类型了
) : CoroutineUseCase<Unit, Boolean>(dispatcher) {

    override suspend fun execute(parameters: Unit): Boolean {

        return preferencesUtil.get("skip", false)
    }
}
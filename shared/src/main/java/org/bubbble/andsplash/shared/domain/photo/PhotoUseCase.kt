package org.bubbble.andsplash.shared.domain.photo

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bubbble.andsplash.model.photo.PhotoInfo
import org.bubbble.andsplash.shared.data.photo.PhotoRepository
import org.bubbble.andsplash.shared.di.IoDispatcher
import org.bubbble.andsplash.shared.domain.MediatorUseCase
import org.bubbble.andsplash.shared.result.Result
import org.bubbble.andsplash.shared.util.logger
import javax.inject.Inject

/**
 * @author Andrew
 * @date 2021/02/06 20:50
 */
class PhotoUseCase @Inject constructor(
    private val repository: PhotoRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): MediatorUseCase<CoroutineScope, List<PhotoInfo>>() {

    override fun execute(parameters: CoroutineScope) {
        try {
            result.removeSource(repository.observerResult)
            result.addSource(repository.observerResult) {
                result.value = Result.Success(it)
            }

            parameters.launch {
//                repository.getAllPhoto()
            }
        } catch (e: Exception) {
            logger(e.message)
            result.value = Result.Error("${e.message}")
        }
    }
}
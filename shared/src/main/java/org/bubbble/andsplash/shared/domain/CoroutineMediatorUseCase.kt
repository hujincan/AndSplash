package org.bubbble.andsplash.shared.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.shared.result.Result

/**
 * @author Andrew
 * @date 2020/10/19 9:51
 * 协程UseCase
 */
abstract class CoroutineMediatorUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    protected val result = MediatorLiveData<Result<R>>()

    /** 异步执行用例并返回一个 [Result].
     *
     * @return a [Result].
     * @param parameters 用于运行用例的输入参数
     *
     * operator 重载操作符的函数执行内容
     */
    suspend operator fun invoke(parameters: P): MediatorLiveData<Result<R>> {
        try {
            // 将所有用例的执行移至注入的调度程序
            // 在生产代码中，这通常是默认调度程序（后台线程）
            withContext(coroutineDispatcher) {
                val request = execute(parameters)

                result.removeSource(request)
                result.addSource(request) {
                    result.postValue(if (it != null) {
                        Result.Success(it)
                    } else {
                        Result.Error("NoData")
                    })
                }
            }
        } catch (e: Exception) {
            result.postValue(Result.Error("${e.message}"))
        }

        return result
    }

    /**
     * 覆盖此设置以设置要执行的代码。
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): LiveData<R>
}
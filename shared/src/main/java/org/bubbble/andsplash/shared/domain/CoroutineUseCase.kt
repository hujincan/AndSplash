package org.bubbble.andsplash.shared.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bubbble.andsplash.shared.result.Result

/**
 * @author Andrew
 * @date 2020/10/19 9:51
 * 协程UseCase
 */
abstract class CoroutineUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    /** 异步执行用例并返回一个 [Result].
     *
     * @return a [Result].
     * @param parameters 用于运行用例的输入参数
     *
     * operator 重载操作符的函数执行内容
     */
    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            // 将所有用例的执行移至注入的调度程序
            // 在生产代码中，这通常是默认调度程序（后台线程）
            // 在测试中，这将成为TestCoroutineDispatcher
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    Result.Success(it)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * 覆盖此设置以设置要执行的代码。
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}
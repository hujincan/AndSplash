package org.bubbble.andsplash.shared.result

import androidx.lifecycle.MutableLiveData
import org.bubbble.andsplash.shared.result.Result.Success

/**
 * @author Andrew
 * @date 2020/10/19 9:53
 * 通用类，其值包含其加载状态。
 * @param <T>
 */

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * 如果[Result]的类型为[Success]且保存为非null [Success.data]，则为true。
 */
val Result<*>.succeeded
    get() = this is Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

/**
 * 如果[Result]的类型为[Success]，则更新[liveData]的值
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.value = data
    }
}

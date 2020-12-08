package org.bubbble.andsplash.shared.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.bubbble.andsplash.shared.data.BuildConfig
import org.bubbble.andsplash.shared.util.PreferencesUtil
import org.bubbble.andsplash.shared.util.logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Auth interceptor
 * 可以将授权信息添加到HTTP请求标头。
 */
class AuthInterceptor (
    private val preferencesUtil: PreferencesUtil) : ReportExceptionInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {

        val accessToken = preferencesUtil.get(BuildConfig.ACCESS_TOKEN, "")

        logger("令牌：$accessToken")

        val request: Request = if (accessToken.isNotEmpty()) {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Client-ID ${BuildConfig.ACCESS_KEY}")
                .build()
        }

        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            handleException(e)
            nullResponse(request)
        }
    }
}
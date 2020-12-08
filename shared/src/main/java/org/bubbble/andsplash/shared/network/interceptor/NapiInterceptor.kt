package org.bubbble.andsplash.shared.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.nio.charset.StandardCharsets

/**
 * Napi interceptor.
 *
 * A interceptor for [retrofit2.Retrofit], it can add header information into
 * HTTP request for Napi network service.
 *
 */
class NapiInterceptor : ReportExceptionInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("authority", "unsplash.com")
            .addHeader("method", "GET")
            .addHeader("scheme", "https")
            .addHeader("accept", "*/*")
            .addHeader("accept-encoding", "gzip, deflate, br")
            .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
            .addHeader("cache-control", "max-age=0")
            .addHeader("upgrade-insecure-requests", "1")
            .addHeader(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"
            )
            .build()
        return try {
            var response: Response = chain.proceed(request)
            response = decodeGZip(request, response)
            response = decodeBr(request, response)
            response
        } catch (e: Exception) {
            handleException(e)
            nullResponse(request)
        }
    }

    @Throws(Exception::class)
    private fun decodeGZip(request: Request, response: Response): Response {
        return response
    }

    @Throws(Exception::class)
    private fun decodeBr(request: Request, response: Response): Response {
        return response
    }

    companion object {
        private val UTF8 = StandardCharsets.UTF_8
    }
}
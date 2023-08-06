package com.disfluency.api.interceptor

import com.disfluency.api.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.getAccessToken()?.let {
            requestBuilder.addHeader("x-auth-secret-key", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
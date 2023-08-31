package com.disfluency.api.interceptor

import com.disfluency.api.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

const val NOT_SEND_AUTH_HEADER = "notSendAuth"

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val shouldNotSendAuth = request.headers[NOT_SEND_AUTH_HEADER] == "true"

        val requestBuilder = request.newBuilder().removeHeader(NOT_SEND_AUTH_HEADER)

        if (!shouldNotSendAuth){
            SessionManager.getAccessToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
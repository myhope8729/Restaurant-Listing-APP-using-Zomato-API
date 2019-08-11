package com.test.zomatoapp.interceptor

import com.test.zomatoapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Content-Type", "application/json; charset=UTF-8")
            ?.addHeader("user-key", BuildConfig.ZOMATO_API_KEY)
            ?.build()
        return chain.proceed(request)
    }
}
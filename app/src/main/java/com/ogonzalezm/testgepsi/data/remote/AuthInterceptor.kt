package com.ogonzalezm.testgepsi.data.remote

import com.ogonzalezm.test_gepsi.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", BuildConfig.RAPID_API_KEY)
            .build()
        return chain.proceed(request)
    }
}
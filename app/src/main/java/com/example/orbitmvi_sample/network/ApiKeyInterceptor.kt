package com.example.orbitmvi_sample.network

import okhttp3.Interceptor
import okhttp3.Response


class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader(Const.AUTH, Const.API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}
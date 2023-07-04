package com.example.todolist.data.remote

import okhttp3.Interceptor
import okhttp3.Response

internal class TokenInterceptor : Interceptor {

    private val token = "Bearer $TOKEN"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTH_HEADER, token)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val TOKEN = "whisperable"
    }
}
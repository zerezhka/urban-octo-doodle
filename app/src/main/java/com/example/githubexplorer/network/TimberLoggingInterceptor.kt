package com.example.githubexplorer.network

import okhttp3.Interceptor
import timber.log.Timber

class TimberLoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        Timber.d("Sending request: ${request.url} on ${chain.connection()} ${request.headers}")
        val response = chain.proceed(request)
        Timber.d("Received response: ${response.request.url} ${response.code} ${response.message}")
        return response
    }
}
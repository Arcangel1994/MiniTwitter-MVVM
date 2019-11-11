package com.example.minitwitter.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN)
        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}
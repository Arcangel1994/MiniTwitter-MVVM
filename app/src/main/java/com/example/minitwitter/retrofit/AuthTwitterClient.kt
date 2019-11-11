package com.example.minitwitter.retrofit

import com.example.minitwitter.common.Constantes
import com.example.minitwitter.retrofit.service.AuthTwitterService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthTwitterClient {
    var authTwitterService: AuthTwitterService
    var retrofit: Retrofit? = null

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(AuthInterceptor())
        val cliente = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente)
            .build()
        authTwitterService = retrofit!!.create(AuthTwitterService::class.java)
    }

    companion object {
        private var instance: AuthTwitterClient? = null
        fun getInstance(): AuthTwitterClient {
            if (instance == null) {
                instance = AuthTwitterClient()
            }
            return instance!!
        }
    }

}

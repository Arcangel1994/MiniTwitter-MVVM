package com.example.minitwitter.retrofit

import com.example.minitwitter.common.Constantes
import com.example.minitwitter.retrofit.service.MiniTwitterService
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


class MiniTwitterClient {

    var miniTwitterService: MiniTwitterService
    var retrofit: Retrofit? = null

    init {

        retrofit = Retrofit.Builder()
            .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        miniTwitterService = retrofit!!.create(MiniTwitterService::class.java)

    }

    companion object {
        private var instance: MiniTwitterClient? = null

        // Patrón Singleton
        fun getInstance(): MiniTwitterClient {
            if (instance == null) {
                instance = MiniTwitterClient()
            }

            return instance!!

        }
    }

}
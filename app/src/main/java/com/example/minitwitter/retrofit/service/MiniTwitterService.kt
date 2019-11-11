package com.example.minitwitter.retrofit.service

import com.example.minitwitter.retrofit.model.request.RequestLogin
import com.example.minitwitter.retrofit.model.request.RequestSignup
import com.example.minitwitter.retrofit.model.response.ResponseAuth
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import com.example.minitwitter.retrofit.model.response.Tweet



interface MiniTwitterService {

    @POST("auth/login")
    fun doLogin(@Body requestLogin: RequestLogin): Call<ResponseAuth>

    @POST("auth/signup")
    fun doSignUp(@Body requestSignup: RequestSignup): Call<ResponseAuth>

}
package com.example.minitwitter.retrofit.service

import com.example.minitwitter.retrofit.model.response.Tweet
import retrofit2.Call
import com.example.minitwitter.retrofit.model.request.RequestCreateTweet
import com.example.minitwitter.retrofit.model.request.RequestUserProfile
import com.example.minitwitter.retrofit.model.response.ResponseUploadPhoto
import com.example.minitwitter.retrofit.model.response.TweetDeleted
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile
import okhttp3.RequestBody
import retrofit2.http.*


interface AuthTwitterService {

    // Tweets
    @GET("tweets/all")
    fun getAllTweets(): Call<List<Tweet>>

    @POST("tweets/create")
    fun createTweet(@Body requestCreateTweet: RequestCreateTweet): Call<Tweet>

    @POST("tweets/like/{idTweet}")
    fun likeTweet(@Path("idTweet") idTweet: Int): Call<Tweet>

    @DELETE("tweets/{idTweet}")
    fun deleteTweet(@Path("idTweet") idTweet: Int): Call<TweetDeleted>

    // Users
    @GET("users/profile")
    fun getProfile(): Call<ResponseUserProfile>

    @PUT("users/profile")
    fun updateProfile(@Body requestUserProfile: RequestUserProfile): Call<ResponseUserProfile>

    @Multipart
    @POST("users/uploadprofilephoto")
    fun uploadProfilePhoto(@Part("file\"; filename=\"photo.jpeg\" ") file: RequestBody): Call<ResponseUploadPhoto>

}
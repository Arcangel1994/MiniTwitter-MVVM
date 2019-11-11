package com.example.minitwitter.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.model.request.RequestUserProfile
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var profileRepository: ProfileRepository? = null

    var userProfile: LiveData<Resource<ResponseUserProfile>>? = MutableLiveData()

    init {

        profileRepository = ProfileRepository(application)
        userProfile = profileRepository!!.getProfile()

    }

    fun updateProfile(requestUserProfile: RequestUserProfile){
        profileRepository!!.updateProfile(requestUserProfile)
    }

    fun uploadPhoto(photo: String){
        profileRepository!!.uploadPhoto(photo)
    }


    fun signOff(){
        profileRepository!!.signOff()
    }

}
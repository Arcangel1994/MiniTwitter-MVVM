package com.example.minitwitter.retrofit.model.response

import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User(

    @SerializedName("id")
    @Expose
    var idUser: Int?,

    @SerializedName("username")
    @Expose
    var usernameUser: String?,

    @SerializedName("descripcion")
    @Expose
    var descripcionUser: String?,

    @SerializedName("website")
    @Expose
    var websiteUser: String?,

    @SerializedName("photoUrl")
    @Expose
    var photoUrlUser: String?,

    @SerializedName("created")
    @Expose
    var createdUser: String?

)
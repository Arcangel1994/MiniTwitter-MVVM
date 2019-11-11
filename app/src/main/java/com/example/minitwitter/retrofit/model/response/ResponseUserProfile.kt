package com.example.minitwitter.retrofit.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "responseuserprofile")
class ResponseUserProfile(

    @SerializedName("id")
    @Expose
    @PrimaryKey
    var id: Int?,

    @SerializedName("username")
    @Expose
    var username: String?,

    @SerializedName("email")
    @Expose
    var email: String?,

    @SerializedName("descripcion")
    @Expose
    var descripcion: String?,

    @SerializedName("website")
    @Expose
    var website: String?,

    @SerializedName("photoUrl")
    @Expose
    var photoUrl: String?,

    @SerializedName("created")
    @Expose
    var created: String?

): Serializable
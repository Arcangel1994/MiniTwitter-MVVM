package com.example.minitwitter.retrofit.model.response

import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Like(

    @SerializedName("id")
    @Expose
    var idLike: Int?,

    @SerializedName("username")
    @Expose
    var usernameLike: String?,

    @SerializedName("descripcion")
    @Expose
    var descripcionLike: String?,

    @SerializedName("website")
    @Expose
    var websiteLike: String?,

    @SerializedName("photoUrl")
    @Expose
    var photoUrlLike: String?,

    @SerializedName("created")
    @Expose
    var createdLike: String?

): Serializable
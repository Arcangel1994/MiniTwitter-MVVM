package com.example.minitwitter.retrofit.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ResponseAuth: Serializable {

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("photoUrl")
    @Expose
    var photoUrl: String? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("active")
    @Expose
    var active: Boolean? = null

    constructor(token: String, username: String, email: String, photoUrl: String, created: String, active: Boolean?) : super() {

        this.token = token
        this.username = username
        this.email = email
        this.photoUrl = photoUrl
        this.created = created
        this.active = active

    }

}
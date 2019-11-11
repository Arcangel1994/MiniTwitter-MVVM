package com.example.minitwitter.retrofit.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RequestUserProfile: Serializable {

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("descripcion")
    @Expose
    var descripcion: String? = null

    @SerializedName("website")
    @Expose
    var website: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    constructor() {}

    constructor(
        username: String,
        email: String,
        descripcion: String,
        website: String,
        password: String
    ) : super() {
        this.username = username
        this.email = email
        this.descripcion = descripcion
        this.website = website
        this.password = password
    }

}
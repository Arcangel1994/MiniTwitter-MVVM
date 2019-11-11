package com.example.minitwitter.retrofit.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RequestLogin: Serializable {

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    constructor(email: String, password: String) : super() {
        this.email = email
        this.password = password
    }

}
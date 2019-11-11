package com.example.minitwitter.retrofit.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RequestSignup: Serializable {

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    constructor(username: String, email: String, password: String, code: String) : super() {

        this.username = username
        this.email = email
        this.password = password
        this.code = code

    }


}
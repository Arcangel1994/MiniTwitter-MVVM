package com.example.minitwitter.retrofit.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TweetDeleted: Serializable{

    @SerializedName("mensaje")
    @Expose
    var mensaje: String? = null

    @SerializedName("user")
    @Expose
    var user: User? = null

    constructor() {}

    constructor(mensaje: String, user: User) : super() {
        this.mensaje = mensaje
        this.user = user
    }

}
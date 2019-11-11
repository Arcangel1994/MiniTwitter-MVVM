package com.example.minitwitter.retrofit.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RequestCreateTweet: Serializable {

    @SerializedName("mensaje")
    @Expose
    var mensaje: String? = null

    constructor() {

    }

    constructor(mensaje: String) : super() {
        this.mensaje = mensaje
    }

}
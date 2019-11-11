package com.example.minitwitter.retrofit.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ResponseUploadPhoto: Serializable {

    @SerializedName("fieldname")
    @Expose
    var fieldname: String? = null

    @SerializedName("originalname")
    @Expose
    var originalname: String? = null

    @SerializedName("encoding")
    @Expose
    var encoding: String? = null

    @SerializedName("mimetype")
    @Expose
    var mimetype: String? = null

    @SerializedName("destination")
    @Expose
    var destination: String? = null

    @SerializedName("filename")
    @Expose
    var filename: String? = null

    @SerializedName("path")
    @Expose
    var path: String? = null

    @SerializedName("size")
    @Expose
    var size: Int? = null

    constructor() {}

    constructor(
        fieldname: String,
        originalname: String,
        encoding: String,
        mimetype: String,
        destination: String,
        filename: String,
        path: String,
        size: Int?
    ) : super() {
        this.fieldname = fieldname
        this.originalname = originalname
        this.encoding = encoding
        this.mimetype = mimetype
        this.destination = destination
        this.filename = filename
        this.path = path
        this.size = size
    }

}
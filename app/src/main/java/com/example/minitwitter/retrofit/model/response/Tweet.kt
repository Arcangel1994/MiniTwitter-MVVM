package com.example.minitwitter.retrofit.model.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "tweet")
class Tweet(

    @PrimaryKey
    var id: Int?,

    var mensaje: String?,

    var likes: List<Like>? = ArrayList<Like>(),

    @Embedded
    var user: User?

): Serializable
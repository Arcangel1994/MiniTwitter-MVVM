package com.example.minitwitter.db.model.Converter

import androidx.room.TypeConverter
import com.example.minitwitter.retrofit.model.response.Like
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LikeDataConverter {

    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Like> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Like>>() {

        }.type

        return gson.fromJson<List<Like>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Like>): String {
        return gson.toJson(someObjects)
    }

}
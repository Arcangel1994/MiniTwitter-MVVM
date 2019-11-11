package com.example.minitwitter.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUserProfile)

    @Update
    fun update(user: ResponseUserProfile)

    @Query("UPDATE responseuserprofile SET photoUrl=:photoUrl WHERE email = :email")
    fun updatePhoto(photoUrl: String, email: String)

    @Query("SELECT * FROM responseuserprofile LIMIT 1")
    fun getUser(): LiveData<ResponseUserProfile>

    /*@Query("DELETE FROM responseuserprofile")
    fun deleteAll()*/

}
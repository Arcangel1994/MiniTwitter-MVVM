package com.example.minitwitter.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minitwitter.retrofit.model.response.Tweet

@Dao
interface TweetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tweet: List<Tweet>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(tweet: Tweet)

    @Update
    fun update(tweet: Tweet)

    @Query("DELETE FROM tweet WHERE id = :id")
    fun deleteOne(id: Int)

    @Query("SELECT * FROM tweet ORDER BY id DESC")
    fun getTweet(): LiveData<List<Tweet>>

    /*@Query("DELETE FROM tweet")
    fun deleteAll()*/

}
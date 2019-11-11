package com.example.minitwitter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.minitwitter.dao.TweetDao
import com.example.minitwitter.dao.UserDAO
import com.example.minitwitter.db.model.Converter.LikeDataConverter
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile
import com.example.minitwitter.retrofit.model.response.Tweet

@Database(entities = [ResponseUserProfile::class, Tweet::class], version = 12, exportSchema = false)
@TypeConverters(LikeDataConverter::class)
abstract class MiniTwitterRoomDatabase: RoomDatabase()  {

    abstract fun userDao(): UserDAO

    abstract fun tweetDao(): TweetDao

    companion object {

        @Volatile
        private var INSTANCE: MiniTwitterRoomDatabase? = null

        fun getDatabase(context: Context): MiniTwitterRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(MiniTwitterRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MiniTwitterRoomDatabase::class.java, "minitwitter_database")
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }

            }

            return INSTANCE

        }
    }

}
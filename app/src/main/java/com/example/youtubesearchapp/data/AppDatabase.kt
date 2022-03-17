package com.example.youtubesearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ID::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun youtubeVideoDAO(): YoutubeVideoDAO
    companion object{
        @Volatile private var instance: AppDatabase? = null
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "Youtube Video List").build()
        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}
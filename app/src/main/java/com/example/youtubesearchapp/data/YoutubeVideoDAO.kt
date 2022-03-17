package com.example.youtubesearchapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao interface YoutubeVideoDAO {
    @Insert suspend fun insert(videoId: ID)
    @Delete suspend fun delete(videoId: ID)
    @Query("Select * From ID") fun getAllLikedVideos(): Flow<List<ID>>
    @Query("Select * From ID Where videoId = :videoId limit 1") fun getLikedVideo(videoId:String): Flow<ID?>
}
package com.example.youtubesearchapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

data class YoutubeVideo(
    val id: ID,
    val snippet: Snippet
) : Serializable

@Entity data class ID(
    @PrimaryKey val videoId: String
) : Serializable

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    @Json(name = "publishedAt") val date: String,
) : Serializable

data class Thumbnails(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail,
) : Serializable

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int,
) : Serializable

data class SingleYoutubeVideo(
    val id: String,
    val snippet: Snippet
)

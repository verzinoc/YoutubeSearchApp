package com.example.youtubesearchapp.data

import com.squareup.moshi.Json
import java.io.Serializable

data class YoutubeVideo(
    val id: ID,
    val snippet: Snippet
) : Serializable

data class ID(
    val videoId: String
) : Serializable

data class Snippet(
    val title: String,
    val description: String,
    val channelTitle: String,
    @Json(name = "publishedAt") val date: String,
) : Serializable

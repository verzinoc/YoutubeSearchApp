package com.example.youtubesearchapp.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    @GET("/youtube/v3/search?type=video&part=snippet&key=AIzaSyA47ewwnQhMyakioY_ukldLEd_oBiaTiAQ&q=")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("sort") sort: String = "date",
        @Query("maxResults") maxResults: String = "50"
    ) : YoutubeSearchResults

    @GET("/youtube/v3/videos?part=snippet")
    suspend fun getVideoByID(
        @Query("id") id: String,
        @Query("key") key: String = "AIzaSyA47ewwnQhMyakioY_ukldLEd_oBiaTiAQ"
    ) : SingleYoutubeVideoResults

    companion object {
        private const val BASE_URL = "https://www.googleapis.com"
        fun create() : YoutubeService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            return retrofit.create(YoutubeService::class.java)
        }
    }
}
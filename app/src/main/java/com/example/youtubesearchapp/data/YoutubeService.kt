package com.example.youtubesearchapp.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    @GET("/search/repositories")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars"
    ) : YoutubeSearchResults

    companion object {
        private const val BASE_URL = "https://api.github.com"
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
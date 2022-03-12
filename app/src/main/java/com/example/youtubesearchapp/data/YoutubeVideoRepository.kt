package com.example.youtubesearchapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class YoutubeVideoRepository(
    private val service: YoutubeService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadVideosSearch(query: String): Result<List<YoutubeVideo>> =
        withContext(ioDispatcher) {
            try {
                val results = service.searchVideos(query)
                Result.success(results.items)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
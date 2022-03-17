package com.example.youtubesearchapp.data

class DatabaseRepository(private val dao: YoutubeVideoDAO) {
    suspend fun insertVideo(youtubeVideo: YoutubeVideo) = dao.insert(youtubeVideo.id)
    suspend fun deleteVideo(youtubeVideo: YoutubeVideo) = dao.delete(youtubeVideo.id)
    fun getAllVideos() = dao.getAllLikedVideos()
    fun getLikedVideo(videoID: String) = dao.getLikedVideo(videoID)
}
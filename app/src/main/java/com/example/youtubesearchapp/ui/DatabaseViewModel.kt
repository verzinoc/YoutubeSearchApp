package com.example.youtubesearchapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.youtubesearchapp.data.AppDatabase
import com.example.youtubesearchapp.data.DatabaseRepository
import com.example.youtubesearchapp.data.YoutubeVideo
import kotlinx.coroutines.launch

class DatabaseViewModel(app: Application): AndroidViewModel(app) {
    private val repository = DatabaseRepository(AppDatabase.getInstance(app).youtubeVideoDAO())
    val likedVideos = repository.getAllVideos().asLiveData()

    fun addLikedVideo(youtubeVideo: YoutubeVideo) {
        viewModelScope.launch {
            repository.insertVideo(youtubeVideo)
        }
    }

    fun deleteLikedVideo(youtubeVideo: YoutubeVideo) {
        viewModelScope.launch {
            repository.deleteVideo(youtubeVideo)
        }
    }

    fun getLikedVideo(videoID: String) = repository.getLikedVideo(videoID).asLiveData()
}

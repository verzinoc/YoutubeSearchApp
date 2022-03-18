package com.example.youtubesearchapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubesearchapp.data.*
import kotlinx.coroutines.launch

class YoutubeSearchViewModel : ViewModel() {
    private val repository = YoutubeVideoRepository(YoutubeService.create())

    private val _searchResults = MutableLiveData<List<YoutubeVideo>?>(null)
    val searchResults: LiveData<List<YoutubeVideo>?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private var _currentSingleVideo: MutableLiveData<SingleYoutubeVideo?> = MutableLiveData()
    val currentSingleVideo: LiveData<SingleYoutubeVideo?> = _currentSingleVideo

    fun loadSearchResults(query: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadVideosSearch(query)
            _searchResults.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }

    fun fetchSingleVideoByID(id: String){
        viewModelScope.launch {
            val result = repository.fetchSingleVideoByID(id)
            //Log.d("check video", result.toString())
            _currentSingleVideo.value = result.getOrNull()?.get(0)
        }
    }
}
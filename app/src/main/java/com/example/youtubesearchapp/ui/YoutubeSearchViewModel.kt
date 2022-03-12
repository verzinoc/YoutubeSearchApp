package com.example.youtubesearchapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubesearchapp.data.LoadingStatus
import com.example.youtubesearchapp.data.YoutubeService
import com.example.youtubesearchapp.data.YoutubeVideo
import com.example.youtubesearchapp.data.YoutubeVideoRepository
import kotlinx.coroutines.launch

class YoutubeSearchViewModel : ViewModel() {
    private val repository = YoutubeVideoRepository(YoutubeService.create())

    private val _searchResults = MutableLiveData<List<YoutubeVideo>?>(null)
    val searchResults: LiveData<List<YoutubeVideo>?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

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
}
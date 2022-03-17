package com.example.youtubesearchapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.youtubesearchapp.R

class LikedVideosActivity : AppCompatActivity() {
    private val viewModel: YoutubeSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_video_list)

        viewModel.fetchSingleVideoByID("5UG4srmI5Sk")
    }
}
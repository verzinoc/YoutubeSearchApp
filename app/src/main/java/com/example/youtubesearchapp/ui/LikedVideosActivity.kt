package com.example.youtubesearchapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubesearchapp.R
import com.example.youtubesearchapp.data.*

class LikedVideosActivity : AppCompatActivity() {
    private val viewModel: YoutubeSearchViewModel by viewModels()
    private lateinit var dataRepository: DatabaseRepository
    private val likedVideosAdapter = LikedVideosAdapter(::onClick)// will need onclick

    private lateinit var likedVideosRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_video_list)

        likedVideosRV = findViewById(R.id.rv_liked_videos)
        likedVideosRV.layoutManager = LinearLayoutManager(this)
        likedVideosRV.adapter = likedVideosAdapter
        likedVideosRV.setHasFixedSize(true)



        dataRepository = DatabaseRepository(AppDatabase.getInstance(this).youtubeVideoDAO())

        dataRepository.getAllVideos().asLiveData().observe(this){ list ->
            val newList = mutableListOf<SingleYoutubeVideo>()
            list.forEach {
                viewModel.fetchSingleVideoByID(it.videoId)
                // this is where updating will be called
                //Log.d("CHECK", viewModel.currentSingleVideo.value.toString())
                val getVideo = viewModel.currentSingleVideo.value
                if (getVideo != null){
                    newList.add(getVideo)
                }
            }
            likedVideosAdapter.updateList(newList)
            //Log.d("updated list", likedVideosAdapter.videosList.toString() + " !! " + newList.toString())
        }

        viewModel.currentSingleVideo.observe(this){
            Log.d("OBSERVED:", it.toString())
            if (it != null) {
                likedVideosAdapter.addVideo(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        likedVideosAdapter.clearVideos()
    }

    private fun onClick(video: SingleYoutubeVideo){
        val newID = ID(video.id)
        val newVideo = YoutubeVideo(newID, video.snippet)
        val intent = Intent(this, VideoDetailActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO, newVideo)
        }
        startActivity(intent)
    }
}
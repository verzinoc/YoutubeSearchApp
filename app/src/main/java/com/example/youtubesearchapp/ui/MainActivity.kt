package com.example.youtubesearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.youtubesearchapp.data.LoadingStatus
import com.example.youtubesearchapp.data.YoutubeSearchResults
import com.example.youtubesearchapp.data.YoutubeVideo
import com.example.youtubesearchapp.ui.EXTRA_YOUTUBE_VIDEO
import com.example.youtubesearchapp.ui.VideoDetailActivity
import com.example.youtubesearchapp.ui.YoutubeSearchViewModel
import com.example.youtubesearchapp.ui.YoutubeVideoListAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    private val apiBaseUrl = "https://www.googleapis.com"
    /*Example query: https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyA47ewwnQhMyakioY_ukldLEd_oBiaTiAQ&q=minecraft */
    private val tag = "MainActivity"

    private val videoListAdapter = YoutubeVideoListAdapter(::onYoutubeVideoClick)
    private val viewModel: YoutubeSearchViewModel by viewModels()

    private lateinit var requestQueue: RequestQueue

    private lateinit var searchBoxET: EditText
    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        //Log.d(tag, "onCreate()")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        searchBoxET = findViewById(R.id.et_search_box)
        searchResultsListRV = findViewById(R.id.rv_search_results)
        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = videoListAdapter

        viewModel.searchResults.observe(this) { searchResults ->
            videoListAdapter.updateVideoList(searchResults)
        }

        viewModel.loadingStatus.observe(this) { loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        val searchBtn: Button = findViewById(R.id.btn_search)
        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                doVideoSearch(query)
                viewModel.loadSearchResults(query)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    override fun onResume() {
        Log.d(tag, "onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d(tag, "onPause()")
        super.onPause()
    }

    private fun doVideoSearch(q: String, sort: String = "date") {
        val url = "$apiBaseUrl/youtube/v3/search?type=video&part=snippet&key=AIzaSyA47ewwnQhMyakioY_ukldLEd_oBiaTiAQ&q=$q"

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<YoutubeSearchResults> =
            moshi.adapter(YoutubeSearchResults::class.java)

        val req = StringRequest(
            Request.Method.GET,
            url,
            {
                val results = jsonAdapter.fromJson(it)
                videoListAdapter.updateVideoList(results?.items)
                loadingIndicator.visibility = View.INVISIBLE
                searchResultsListRV.visibility = View.VISIBLE
            },
            {
                Log.d(tag, "Error fetching from $url: ${it.message}")
                loadingIndicator.visibility = View.INVISIBLE
                searchErrorTV.visibility = View.VISIBLE
            }
        )

        loadingIndicator.visibility = View.VISIBLE
        searchResultsListRV.visibility = View.INVISIBLE
        searchErrorTV.visibility = View.INVISIBLE
        requestQueue.add(req)
    }



    private fun onYoutubeVideoClick(video: YoutubeVideo) {
        val intent = Intent(this, VideoDetailActivity::class.java).apply {
            putExtra(EXTRA_YOUTUBE_VIDEO, video)
        }
        startActivity(intent)
    }
}
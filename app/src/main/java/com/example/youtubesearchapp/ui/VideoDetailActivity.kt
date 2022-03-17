package com.example.youtubesearchapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.youtubesearchapp.R
import com.example.youtubesearchapp.data.AppDatabase
import com.example.youtubesearchapp.data.DatabaseRepository
import com.example.youtubesearchapp.data.YoutubeVideo

const val EXTRA_YOUTUBE_VIDEO = "com.example.android.lifecyclegithubsearch.GitHubRepo"

class VideoDetailActivity : AppCompatActivity() {
    private var video: YoutubeVideo? = null
    private val youtubeEmbedUrl = "https://www.youtube.com/embed/"

    private lateinit var videosDB: DatabaseRepository
    private var isLiked = false

    private val viewModel: DatabaseViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        videosDB = DatabaseRepository(AppDatabase.getInstance(this).youtubeVideoDAO())

        if (intent != null && intent.hasExtra(EXTRA_YOUTUBE_VIDEO)) {
            video = intent.getSerializableExtra(EXTRA_YOUTUBE_VIDEO) as YoutubeVideo
            findViewById<TextView>(R.id.tv_video_name).text = video!!.snippet.title
            findViewById<TextView>(R.id.tv_video_channel).text = video!!.snippet.channelTitle
            findViewById<TextView>(R.id.tv_video_description).text = video!!.snippet.description

            val webView: WebView = findViewById(R.id.video_player)
            val settings = webView.settings
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webView.loadUrl(youtubeEmbedUrl + video!!.id.videoId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_video_detail, menu)

        val likeVideo = menu.findItem(R.id.action_like_video)
        viewModel.getLikedVideo(video!!.id.videoId).observe(this){
            when (it){
                null -> {
                    isLiked = false
                    likeVideo.icon = AppCompatResources.getDrawable(this, R.drawable.ic_outline_thumb_up)
                }
                else -> {
                    isLiked = true
                    likeVideo.icon = AppCompatResources.getDrawable(this, R.drawable.ic_action_liked_video_list)
                }
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_view_video -> {
                viewVideoOnWeb()
                true
            }
            R.id.action_share -> {
                shareVideo()
                true
            }
            R.id.action_like_video -> {
                Log.d("WOOHOO GOT A LIKE BABYY", "SMASH THAT LIKE BUTTON")
                if (isLiked){
                    video?.let { viewModel.deleteLikedVideo(it) }
                }
                else{
                    video?.let { viewModel.addLikedVideo(it) }
                }

                //video?.let { videosDB.insertVideo(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewVideoOnWeb() {
//        if (video != null) {
//            val intent: Intent = Uri.parse(video!!.url).let {
//                Intent(Intent.ACTION_VIEW, it)
//            }
//            try {
//                startActivity(intent)
//            } catch (e: ActivityNotFoundException) {
//                Snackbar.make(
//                    findViewById(R.id.coordinator_layout),
//                    R.string.action_view_video_error,
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
    }

    private fun shareVideo() {
        if (video != null) {
            val text = getString(R.string.share_text, video!!.snippet.title, video!!.snippet.channelTitle, video!!.id.videoId)
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
}
package com.example.youtubesearchapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.youtubesearchapp.data.YoutubeVideo
import com.google.android.material.snackbar.Snackbar
import com.example.youtubesearchapp.R

const val EXTRA_YOUTUBE_VIDEO = "com.example.android.lifecyclegithubsearch.GitHubRepo"

class VideoDetailActivity : AppCompatActivity() {
    private var video: YoutubeVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        if (intent != null && intent.hasExtra(EXTRA_YOUTUBE_VIDEO)) {
            video = intent.getSerializableExtra(EXTRA_YOUTUBE_VIDEO) as YoutubeVideo
            findViewById<TextView>(R.id.tv_video_name).text = video!!.snippet.title
            findViewById<TextView>(R.id.tv_video_stars).text = video!!.snippet.channelTitle
            findViewById<TextView>(R.id.tv_video_description).text = video!!.snippet.description
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_video_detail, menu)
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
            val text = getString(R.string.share_text, video!!.snippet.title, video!!.snippet.channelTitle)
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }
}
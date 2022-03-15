package com.example.youtubesearchapp.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubesearchapp.data.YoutubeVideo
import com.example.youtubesearchapp.R


class YoutubeVideoListAdapter(private val onYoutubeVideoClick: (YoutubeVideo) -> Unit)
    : RecyclerView.Adapter<YoutubeVideoListAdapter.YoutubeVideoViewHolder>() {
    var youtubeVideoList = listOf<YoutubeVideo>()

    fun updateVideoList(newVideoList: List<YoutubeVideo>?) {
        youtubeVideoList = newVideoList ?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = youtubeVideoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeVideoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.youtube_video_list_item, parent, false)
        return YoutubeVideoViewHolder(itemView, onYoutubeVideoClick)
    }

    override fun onBindViewHolder(holder: YoutubeVideoViewHolder, position: Int) {
        holder.bind(youtubeVideoList[position])
    }

    class YoutubeVideoViewHolder(itemView: View, private val onClick: (YoutubeVideo) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private val thumbnailIV: ImageView = itemView.findViewById(R.id.details_thumbnail)
        private var currentYoutubeVideo: YoutubeVideo? = null

        init {
            itemView.setOnClickListener {
                currentYoutubeVideo?.let(onClick)
            }
        }

        @SuppressLint("StaticFieldLeak")
        private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
            override fun doInBackground(vararg urls: String): Bitmap? {
                val imageURL = urls[0]
                var image: Bitmap? = null
                try {
                    val `in` = java.net.URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                }
                catch (e: Exception) {
                    Log.e("Error Message", e.message.toString())
                    e.printStackTrace()
                }
                return image
            }
            override fun onPostExecute(result: Bitmap?) {
                imageView.setImageBitmap(result)
            }
        }

        fun bind(youtubeVideo: YoutubeVideo) {
            currentYoutubeVideo = youtubeVideo
            nameTV.text = youtubeVideo.snippet.title
            DownloadImageFromInternet(thumbnailIV).execute(youtubeVideo.snippet.thumbnails.high.url)
        }
    }
}
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
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubesearchapp.R
import com.example.youtubesearchapp.data.SingleYoutubeVideo

class LikedVideosAdapter(private val onClick: (SingleYoutubeVideo) -> Unit)
    : RecyclerView.Adapter<LikedVideosAdapter.ViewHolder>() {
    var videosList = mutableListOf<SingleYoutubeVideo>()

    inner class ViewHolder(view: View, private val onClick: (SingleYoutubeVideo) -> Unit)
        : RecyclerView.ViewHolder(view){
        private var currentVideo: SingleYoutubeVideo? = null
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private val thumbnailIV: ImageView = itemView.findViewById(R.id.details_thumbnail)

        init {
            itemView.setOnClickListener{
                currentVideo?.let(onClick)
            }
        }

        fun bind(video: SingleYoutubeVideo){
            currentVideo = video
            nameTV.text = replaceAscii(video.snippet.title)
            DownloadImageFromInternet(thumbnailIV).execute(video.snippet.thumbnails.high.url)
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

        private fun replaceAscii(string: String): String {
            var string2 = string
            while (string2.contains("&#39;")) {
                string2 = string2.replace("&#39;", "'")
            }
            return string2
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<SingleYoutubeVideo>?){
        videosList = list ?: mutableListOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.youtube_video_list_item, parent, false)
        return ViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videosList[position])
    }

    override fun getItemCount(): Int = videosList.size

    @SuppressLint("NotifyDataSetChanged")
    fun clearVideos() {
        videosList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addVideo(video: SingleYoutubeVideo){
        videosList.add(video)
        notifyDataSetChanged()
    }
}
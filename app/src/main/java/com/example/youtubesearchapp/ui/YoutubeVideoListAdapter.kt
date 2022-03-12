package com.example.youtubesearchapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    class YoutubeVideoViewHolder(itemView: View, val onClick: (YoutubeVideo) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private var currentYoutubeVideo: YoutubeVideo? = null

        init {
            itemView.setOnClickListener {
                currentYoutubeVideo?.let(onClick)
            }
        }

        fun bind(youtubeVideo: YoutubeVideo) {
            currentYoutubeVideo = youtubeVideo
            nameTV.text = youtubeVideo.name
        }
    }
}
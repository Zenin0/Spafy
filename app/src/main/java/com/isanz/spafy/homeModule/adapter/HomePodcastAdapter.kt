package com.isanz.spafy.homeModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.isanz.spafy.R
import com.isanz.spafy.common.entities.Podcast
import com.isanz.spafy.common.utils.Constants

class HomePodcastAdapter(private val context: Context) :
    ListAdapter<Podcast, HomePodcastAdapter.PodcastViewHolder>(PodcastDiffCallback()) {

    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_preview_home, parent, false)
        return PodcastViewHolder(view)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val podcast = getItem(position)
        holder.title.text = podcast.titulo
        setImage(holder.imageView, Constants.IMAGE_PODCAST_URL)
    }

    class PodcastDiffCallback : DiffUtil.ItemCallback<Podcast>() {
        override fun areItemsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem == newItem
        }
    }

    private fun setImage(view: ImageView, uri: String) {
        Glide.with(context).load(uri).transform(CircleCrop()).into(view)
    }
}
package com.isanz.spafy.searchModule.adapter

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
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants

class SearchListAdapter(private val context: Context) :
    ListAdapter<PlayList, SearchListAdapter.PlayListViewHolder>(PlayListDiffCallback()) {

    class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val numCanciones: TextView = itemView.findViewById(R.id.numCanciones)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return PlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playList = getItem(position)
        holder.title.text = playList.titulo
        holder.numCanciones.text = playList.numeroCanciones.toString()
        setImage(holder.imageView, Constants.IMAGE_PLAYLIST_URL)
    }

    class PlayListDiffCallback : DiffUtil.ItemCallback<PlayList>() {
        override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
            return oldItem == newItem
        }
    }

    private fun setImage(view: ImageView, uri: String) {
        Glide.with(context).load(uri).transform(CircleCrop()).into(view)
    }
}
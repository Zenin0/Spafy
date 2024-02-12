package com.isanz.spafy.libraryModule.adapter

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
import com.isanz.spafy.common.utils.IOnItemClickListener

class LibraryPlaylistAdapter(
    private val context: Context,
    private val listener: IOnItemClickListener
) :
    ListAdapter<PlayList, LibraryPlaylistAdapter.ViewHolder>(PlayListDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val canciones: TextView = itemView.findViewById(R.id.duracion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playList = getItem(position)
        holder.title.text = playList.titulo
        "Canciones: ${playList.numeroCanciones}".also { holder.canciones.text = it }
        setImage(holder.imageView, Constants.IMAGE_PLAYLIST_URL)
        holder.itemView.setOnClickListener {
            listener.onItemClick(playList)
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongItemClick(playList)
            true
        }
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

    fun removePlaylist(playlist: PlayList) {
        val currentList = currentList.toMutableList()
        currentList.remove(playlist)
        submitList(currentList)
    }
}
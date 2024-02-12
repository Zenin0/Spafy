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
import com.isanz.spafy.R
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener

class AddToPlaylistAdapter(
    private val context: Context,
    private val listener: IOnItemClickListener
) :
    ListAdapter<PlayList, AddToPlaylistAdapter.ViewHolder>(PlayListDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivPlaylistImage)
        val tittlePlaylist: TextView = itemView.findViewById(R.id.ivPlaylistName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_add, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playList = getItem(position)
        holder.tittlePlaylist.text = playList.titulo
        setImage(holder.imageView, Constants.IMAGE_PLAYLIST_URL)

        // Set an OnClickListener on the ViewHolder's itemView
        holder.itemView.setOnClickListener {
            listener.onItemClick(playList)
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
        Glide.with(context).load(uri).transform().into(view)
    }
}
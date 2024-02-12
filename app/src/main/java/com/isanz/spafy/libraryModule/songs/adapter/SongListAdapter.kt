package com.isanz.spafy.libraryModule.songs.adapter

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
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener

class SongListAdapter(private val context: Context, private val listener: IOnItemClickListener) :
    ListAdapter<Cancion, SongListAdapter.ViewHolder>(PlayListDiffCallback()) {

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
        val cancion = getItem(position)
        holder.title.text = cancion.titulo
        val duracion = if (cancion.duracion % 60 < 10) {
            "${cancion.duracion / 60}:0${cancion.duracion % 60}"
        } else {
            "${cancion.duracion / 60}:${cancion.duracion % 60}"
        }
        holder.canciones.text = duracion
        setImage(holder.imageView, Constants.IMAGE_PLAYLIST_URL)
        holder.itemView.setOnLongClickListener() {
            listener.onLongItemClick(cancion)
            true
        }
    }

    class PlayListDiffCallback : DiffUtil.ItemCallback<Cancion>() {
        override fun areItemsTheSame(oldItem: Cancion, newItem: Cancion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cancion, newItem: Cancion): Boolean {
            return oldItem == newItem
        }
    }

    private fun setImage(view: ImageView, uri: String) {
        Glide.with(context).load(uri).transform(CircleCrop()).into(view)
    }

    fun removeSong(cancion: Cancion) {
        val currentList = currentList.toMutableList()
        currentList.remove(cancion)
        submitList(currentList)
    }
}
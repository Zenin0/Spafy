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
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.utils.Constants

class SearchListAdapter(private val context: Context) :
    ListAdapter<Cancion, SearchListAdapter.CancionViewHolder>(CancionDiffCallback()) {

    class CancionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val duracion: TextView = itemView.findViewById(R.id.duracion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return CancionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {
        val cancion = getItem(position)
        holder.title.text = cancion.titulo
        val minutes = cancion.duracion / 60
        val seconds = cancion.duracion % 60
        "$minutes:$seconds".also { holder.duracion.text = it }
        setImage(holder.imageView, Constants.IMAGE_PLAYLIST_URL)
    }

    class CancionDiffCallback : DiffUtil.ItemCallback<Cancion>() {
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
}
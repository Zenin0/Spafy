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
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener

class HomePlaylistAdapter(private val context: Context, private val listener: IOnItemClickListener):
    ListAdapter<PlayList, HomePlaylistAdapter.ViewHolder>(PlayListDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val desc: TextView = itemView.findViewById(R.id.subTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_preview_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playList = getItem(position)
        holder.title.text = playList.titulo
        holder.desc.text = context.getString(R.string.playlist)
        holder.itemView.setOnClickListener {
            listener.onItemClick(playList)
        }
        setImage(holder.imageView)
    }

    class PlayListDiffCallback : DiffUtil.ItemCallback<PlayList>() {
        override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
            return oldItem == newItem
        }
    }

    private fun setImage(view: ImageView) {
        val uri = Constants.IMAGES.random()
        Glide.with(context).load(uri).into(view)
    }
}
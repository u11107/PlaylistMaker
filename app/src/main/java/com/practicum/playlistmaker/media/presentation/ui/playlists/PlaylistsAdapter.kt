package com.practicum.playlistmaker.media.presentation.ui.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistsAdapter(private val items: ArrayList<Playlist>):
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    var clickListener: PlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = items[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener{clickListener?.onPlaylistClick(playlist)}
    }

    fun addItems(values: List<Playlist>) {
        this.items.clear()
        if (values.isNotEmpty()) {
            this.items.addAll(values)
        }
        this.notifyDataSetChanged()
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
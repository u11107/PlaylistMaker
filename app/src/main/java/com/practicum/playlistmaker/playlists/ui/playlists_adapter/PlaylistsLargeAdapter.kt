package com.practicum.playlistmaker.playlists.ui.playlists_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist

class PlaylistsLargeAdapter(private val clickListener: PlaylistLargeClickListener) :
    RecyclerView.Adapter<PlaylistLargeViewHolder>() {

    val playlists = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistLargeViewHolder =
        PlaylistLargeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_card_view_large, parent, false)
        )

    override fun onBindViewHolder(holder: PlaylistLargeViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClickListener(playlists[position]) }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface PlaylistLargeClickListener {
        fun onPlaylistClickListener(playlist: Playlist)
    }
}
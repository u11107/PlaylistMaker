package com.practicum.playlistmaker.playlists.ui.playlists_adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist


class PlaylistLargeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cover: ImageView = itemView.findViewById(R.id.playlist_cover_large)
    private val name: TextView = itemView.findViewById(R.id.playlist_name_large)
    private val numberOfTracks: TextView = itemView.findViewById(R.id.number_of_tracks_large)

    fun bind(playlist: Playlist) {
        Glide.with(cover)
            .load(playlist.coverUri)
            .placeholder(R.drawable.ic_track_placeholder_large)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corners_album))
            )
            .into(cover)
        name.text = playlist.name
        numberOfTracks.text =
            itemView.resources.getQuantityString(
                R.plurals.plurals_tracks,
                playlist.numberOfTracks,
                playlist.numberOfTracks
            )
    }

}
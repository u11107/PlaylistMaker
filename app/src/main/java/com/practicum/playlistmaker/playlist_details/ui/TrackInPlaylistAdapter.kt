package com.practicum.playlistmaker.playlist_details.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter


class TrackInPlaylistAdapter(private val clickListener: TrackClickListener) :
    TrackAdapter<TrackInPlaylistViewHolder>(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackInPlaylistViewHolder =
        TrackInPlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
        )

    override fun onBindViewHolder(holder: TrackInPlaylistViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener { clickListener.onTrackLongClickListener(trackList[position]) }
        super.onBindViewHolder(holder, position)
    }

    interface TrackClickListener : TrackAdapter.TrackClickListener {
        fun onTrackLongClickListener(track: Track): Boolean
    }
}
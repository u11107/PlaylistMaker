package com.practicum.playlistmaker.playlist_details.ui

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackViewHolder
import com.practicum.playlistmaker.utils.DateUtils

class TrackInPlaylistViewHolder(itemView: View) : TrackViewHolder(itemView) {

    override fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = DateUtils.formatTime(model.duration)
        Glide.with(artwork)
            .load(model.lowResArtworkUri)
            .transform(
                FitCenter(),
                RoundedCorners(
                    itemView.resources
                        .getDimensionPixelSize(R.dimen.rounded_corners_album_preview)
                )
            )
            .placeholder(R.drawable.ic_track_placeholder_small)
            .into(artwork)
    }
}
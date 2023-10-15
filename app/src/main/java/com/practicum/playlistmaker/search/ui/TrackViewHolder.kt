package com.practicum.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.DateUtils

open class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val trackName: TextView = itemView.findViewById(R.id.track_name)
    protected val artistName: TextView = itemView.findViewById(R.id.artist_name)
    protected val trackTime: TextView = itemView.findViewById(R.id.track_time)
    protected val artwork: ImageView = itemView.findViewById(R.id.album_artwork)

    open fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = DateUtils.formatTime(model.duration)
        Glide.with(artwork)
            .load(model.midResArtworkUri)
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
package com.practicum.playlistmaker.search.presentation.ui

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_view, parentView, false)
) {

    private val trackNameView: TextView by lazy { itemView.findViewById(R.id.trackName) }
    private val trackInfoView: TextView by lazy { itemView.findViewById(R.id.trackInfo) }
    private val trackImageView: ImageView by lazy { itemView.findViewById(R.id.trackImage) }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        trackInfoView.text = itemView.context.getString(
            R.string.track_info,
            model.artistName,
            model.getTrackTime()
        )
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_track)
            .centerCrop()
            .transform(RoundedCorners((itemView.resources.getDimension(R.dimen.small_album_round_corners) * (itemView.resources
                .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()))
            .into(trackImageView)
    }
}
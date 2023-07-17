package com.example.playlistmaker.search.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackVBinding
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = TrackVBinding.bind(item)

    fun bind(track: Track) = with(binding) {
        nameTrack.text = track.trackName
        executor.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis)
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    itemView.resources
                        .getDimensionPixelOffset(R.dimen.track_album_image_radius_2)
                )
            )
            .into(albumIm)
    }
}
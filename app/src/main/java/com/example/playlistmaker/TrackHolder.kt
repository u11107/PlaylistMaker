package com.example.playlistmaker

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.model.Track

class TrackHolder(item: View):RecyclerView.ViewHolder(item) {
    private val binding = TrackViewBinding.bind(item)

    fun bind(track:Track) = with(binding) {
        nameTrack.text = track.trackName
        executor.text = track.artistName
        duration.text = track.trackTime
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources
                .getDimensionPixelOffset(R.dimen.track_album_image_radius)))
            .into(albumIm)
    }
}
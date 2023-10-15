package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track

open class TrackAdapter<T : TrackViewHolder>(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<T>() {

    var trackList = ArrayList<Track>()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T =
        TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
        ) as T

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClickListener(trackList[position]) }
    }

    interface TrackClickListener {
        fun onTrackClickListener(track: Track)
    }

}
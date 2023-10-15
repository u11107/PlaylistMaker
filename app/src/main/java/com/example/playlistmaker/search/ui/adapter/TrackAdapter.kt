package com.example.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.holder.TrackHolder
import com.example.playlistmaker.search.ui.tracksDiff.TracksDiffCallback


class TrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackHolder>() {

    var tracks = mutableListOf<Track>()
        set(newTracks) {
            val diffCallback = TracksDiffCallback(field, newTracks)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newTracks
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_v, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun clearTracks () {
        tracks = ArrayList()
    }
}
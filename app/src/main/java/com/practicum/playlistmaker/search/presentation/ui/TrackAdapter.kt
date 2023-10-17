package com.practicum.playlistmaker.search.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter(private var items: ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder>() {
    var clickListener: TrackClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = items[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{clickListener?.onTrackClick(track)}
    }

    fun addItems(values: ArrayList<Track>) {
        this.items.clear()
        if (values.size > 0) {
            this.items.addAll(values)
        }
        this.notifyDataSetChanged()
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}
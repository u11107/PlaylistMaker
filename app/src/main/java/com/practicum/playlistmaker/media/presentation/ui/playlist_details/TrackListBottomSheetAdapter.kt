package com.practicum.playlistmaker.media.presentation.ui.playlist_details

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track

class TrackListBottomSheetAdapter(private var items: ArrayList<Track>) : RecyclerView.Adapter<TrackListBottomSheetViewHolder>() {
    var clickListener: TrackClickListener? = null
    var longClickListener: TrackLongClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListBottomSheetViewHolder {
        return TrackListBottomSheetViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TrackListBottomSheetViewHolder, position: Int) {
        val track = items[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{clickListener?.onTrackClick(track)}
        holder.itemView.setOnLongClickListener {
            longClickListener?.onTrackLongClick(track)
            return@setOnLongClickListener true
        }
    }

    fun addItems(values: List<Track>) {
        this.items.clear()
        if (values.isNotEmpty()) {
            this.items.addAll(values)
        }
        this.notifyDataSetChanged()
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface TrackLongClickListener {
        fun onTrackLongClick(track: Track)
    }
}
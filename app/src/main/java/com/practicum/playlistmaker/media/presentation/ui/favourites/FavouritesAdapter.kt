package com.practicum.playlistmaker.media.presentation.ui.favourites

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track

class FavouritesAdapter(private var items: ArrayList<Track>) : RecyclerView.Adapter<FavouritesViewHolder>() {
    var clickListener: TrackClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        return FavouritesViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val track = items[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{clickListener?.onTrackClick(track)}
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
}
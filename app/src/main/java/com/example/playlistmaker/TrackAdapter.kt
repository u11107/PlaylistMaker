package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory.addTrack
import com.example.playlistmaker.model.Track


class TrackAdapter(private val clickListener: MovieClickListener) : RecyclerView.Adapter<TrackHolder>() {

    var trackList = ArrayList<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_v, parent, false)
        return TrackHolder(view)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val trackPosition  = trackList[position]
        holder.bind(trackPosition)
        holder.itemView.setOnClickListener {
            addTrack(trackPosition)
            clickListener.onMovieClick(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun interface MovieClickListener {
        fun onMovieClick(track: Track)
    }

}
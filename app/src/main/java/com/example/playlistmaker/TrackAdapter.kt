package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.Companion.TRACK
import com.example.playlistmaker.SearchHistory.addTrack
import com.example.playlistmaker.model.Track


class TrackAdapter() : RecyclerView.Adapter<TrackHolder>() {

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
            val intent = Intent(it.context, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK, trackPosition)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }


}
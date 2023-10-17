package com.practicum.playlistmaker.player.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistListItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class BottomSheetAdapter(private val items: ArrayList<Playlist>):
    RecyclerView.Adapter<BottomSheetViewHolder>() {
    var clickListener: PlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding =
            PlaylistListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.vClickable = true
        return BottomSheetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val playlist = items[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener{clickListener?.onPlaylistClick(playlist)}
    }

    fun addItems(values: List<Playlist>) {
        this.items.clear()
        if (values.isNotEmpty()) {
            this.items.addAll(values)
        }
        this.notifyDataSetChanged()
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
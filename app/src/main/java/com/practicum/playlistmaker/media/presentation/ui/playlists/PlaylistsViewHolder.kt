package com.practicum.playlistmaker.media.presentation.ui.playlists

import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.ui.playlist.PlaylistFragment
import com.practicum.playlistmaker.utils.getTrackCountNoun
import java.io.File

class PlaylistsViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_grid_item, parentView, false)
) {
    private val playlistImageView: ImageView by lazy { itemView.findViewById(R.id.ivPlaylistGridItem)}
    private val playlistNameView: TextView by lazy { itemView.findViewById(R.id.tvPlaylistGridItemName)}
    private val playlistTrackInfoView: TextView by lazy { itemView.findViewById(R.id.tvPlaylistGridItemTrackInfo)}

    fun bind(model: Playlist) {
        playlistNameView.text = model.name.toString()
        playlistTrackInfoView.text = String.format("%d %s", model.trackCount, getTrackCountNoun(model.trackCount))

        playlistImageView.setImageResource(R.drawable.ic_playlist)
        if (!model.filePath.isNullOrEmpty()) {
            val filePath = File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PlaylistFragment.IMAGE_DIR
            )
            val file = File(filePath, model.filePath!!)
            playlistImageView.setImageURI(Uri.fromFile(file))
            playlistImageView.clipToOutline = true
        }

    }
}
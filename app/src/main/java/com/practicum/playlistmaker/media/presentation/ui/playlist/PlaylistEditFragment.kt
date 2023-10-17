package com.practicum.playlistmaker.media.presentation.ui.playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.mapper.ParcelablePlaylistMapper
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenResult
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistEditFragment: PlaylistFragment() {
    private val args: PlaylistEditFragmentArgs by navArgs()

    override val viewModel: PlaylistEditViewModel by viewModel {
        parametersOf(ParcelablePlaylistMapper.map(args.playlist))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewAttributes()

        val playlist = ParcelablePlaylistMapper.map(args.playlist)
        initPlaylistData(playlist)
    }

    private fun initPlaylistData(playlist: Playlist) {
        binding.edPlaylistName.editText?.setText(playlist.name)
        binding.edPlaylistDescription.editText?.setText(playlist.description)
        binding.playlistImageView.setImageResource(R.drawable.ic_playlist)
        if (!playlist.filePath.isNullOrEmpty()) {
            val file = playlist.filePath?.let { File(viewModel.getImageDirectory(), it) }
            binding.playlistImageView.setImageURI(Uri.fromFile(file))
            binding.playlistImageView.clipToOutline = true
        }
    }

    private fun setViewAttributes() {
        binding.btCreatePlaylist.setText(R.string.button_save_playlist)
        binding.tvPlaylistTitle.setText(R.string.button_edit_playlist)
    }

    override fun renderResult(result: PlaylistScreenResult) {
        when (result) {
            is PlaylistScreenResult.Canceled -> privateNavigateUp()
            is PlaylistScreenResult.Created -> privateNavigateUp()
        }
    }
}
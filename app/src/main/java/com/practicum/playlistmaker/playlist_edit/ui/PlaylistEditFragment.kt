package com.practicum.playlistmaker.playlist_edit.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist_creation.ui.PlaylistCreationFragment
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistDetails
import com.practicum.playlistmaker.playlist_edit.view_model.PlaylistEditStringData
import com.practicum.playlistmaker.playlist_edit.view_model.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditFragment : PlaylistCreationFragment() {

    private var playlistId = 0
    override val viewModel: PlaylistEditViewModel by viewModel {
        parametersOf(playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playlistId = requireArguments().getInt(PLAYLIST_ARG)
        super.onViewCreated(view, savedInstanceState)

        viewModel.initScreen()

        viewModel.observePlaylistData().observe(viewLifecycleOwner) { data ->
            renderPlaylistData(data)
        }

        viewModel.observePlaylistEditStringData().observe(viewLifecycleOwner) { data ->
            overrideScreenStrings(data)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun playlistCreatedSnackbar(view: View) {
        val snackbar = Snackbar.make(
            requireContext(),
            view,
            "${getString(R.string.playlist)} ${binding.playlistName.text} ${getString(R.string.saved)}",
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.black_light_white_night
            )
        )
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            textSize = resources.getDimension(R.dimen.snackbar_text_size)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeface = Typeface.DEFAULT
        }
        snackbar.show()
    }

    private fun renderPlaylistData(data: PlaylistDetails) {
        setPlaylistCover(data.coverUri)
        with(binding) {
            playlistName.setText(data.name)
            playlistDescription.setText(data.description)
        }
    }

    private fun overrideScreenStrings(data: PlaylistEditStringData) {
        with(binding) {
            screenName.text = data.screenDescription
            createPlaylist.text = data.saveChangesButtonText
        }
    }

    private fun setPlaylistCover(coverUri: String) {
        Glide.with(binding.plCover)
            .load(coverUri)
            .placeholder(R.drawable.ic_track_placeholder_large)
            .centerCrop()
            .into(binding.plCover)
    }

    companion object {
        fun createArgs(playlistId: Int): Bundle = bundleOf(PLAYLIST_ARG to playlistId)
        private const val PLAYLIST_ARG = "PLAYLIST_ARG"
    }
}
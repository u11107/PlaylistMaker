package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.ui.playlists_adapter.PlaylistsSmallAdapter
import com.practicum.playlistmaker.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.view_model.PlaylistsInPlayerState
import com.practicum.playlistmaker.player.view_model.ToastState
import com.practicum.playlistmaker.playlist_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.DateUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private lateinit var track: Track
    private lateinit var binding: FragmentPlayerBinding
    private var playlistsAdapter = PlaylistsSmallAdapter {
        viewModel.onPlaylistClicked(it)
    }
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private val gson: Gson by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = gson.fromJson(
            requireArguments().getString(TRACK_ARG),
            object : TypeToken<Track>() {}.type
        )

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.playerOverlay.visibility = when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> View.GONE
                    else -> View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        with(binding) {
            playlistsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = playlistsAdapter
            }
            playImage.isEnabled = false
            playImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_play_button
                )
            )
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackDuration.text = DateUtils.formatTime(track.duration)
            trackGenre.text = track.genre
            trackCountry.text = track.country
            trackYear.text = track.releaseYear
        }

        if (track.album == "") {
            binding.trackAlbum.visibility = View.GONE
            binding.album.visibility = View.GONE
        } else {
            binding.trackAlbum.text = track.album
        }

        setFavoriteButton(track.isFavorite)

        val artworkUriHighRes = track.highResArtworkUri
        Glide.with(binding.coverImage)
            .load(artworkUriHighRes)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    binding.coverImage.resources
                        .getDimensionPixelSize(R.dimen.rounded_corners_album)
                )
            )
            .placeholder(R.drawable.ic_track_placeholder_small)
            .into(binding.coverImage)

        viewModel.observeState().observe(viewLifecycleOwner) {
            binding.playImage.isEnabled = it.isPlayButtonEnabled
            binding.currentPlaybackTime.text = it.progress
            setPlayOrPauseImage(it.buttonText)
        }
        viewModel.observeToastState().observe(viewLifecycleOwner) { toastState ->
            if (toastState is ToastState.Show) {
                noPreviewUrlMessage(toastState.additionalMessage)
                viewModel.toastWasShown()
            }
        }
        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            setFavoriteButton(isFavorite)
        }
        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsInPlayerState.DisplayPlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    binding.playerOverlay.visibility = View.VISIBLE
                    displayPlaylists(it.playlists)
                }

                is PlaylistsInPlayerState.HidePlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.playerOverlay.visibility = View.GONE
                }
            }
        }
        viewModel.preparePlayer()
        binding.playImage.setOnClickListener {
            viewModel.onPlayPressed()
        }

        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.favoriteImage.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.addToPlaylistClicked()
        }

        binding.createPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistCreationFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun noPreviewUrlMessage(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setPlayOrPauseImage(buttonText: String) {
        when (buttonText) {
            PLAY_BUTTON ->
                binding.playImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_play_button
                    )
                )

            else ->
                binding.playImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_pause_button
                    )
                )
        }
    }

    private fun setFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_remove_from_favorites
                )
            )
        } else {
            binding.favoriteImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_add_to_favorites
                )
            )
        }
    }

    private fun displayPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    companion object {
        fun createArgs(trackJson: String): Bundle = bundleOf(TRACK_ARG to trackJson)
        private const val TRACK_ARG = "TRACK_ARG"
        const val PLAY_BUTTON = "PLAY_BUTTON"
    }
}
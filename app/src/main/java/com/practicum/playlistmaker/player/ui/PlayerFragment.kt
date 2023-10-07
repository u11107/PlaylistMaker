package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.playlists_adapter.PlaylistsSmallAdapter
import com.practicum.playlistmaker.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.view_model.PlaylistsInPlayerState
import com.practicum.playlistmaker.player.view_model.ToastState
import com.practicum.playlistmaker.playlists_creation.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private lateinit var currentPlaybackTime: TextView
    private lateinit var playImageView: ImageView
    private lateinit var track: Track
    private lateinit var favoriteImageView: ImageView
    private lateinit var playlistsRecyclerView: RecyclerView

    private var playlistsAdapter = PlaylistsSmallAdapter {
        viewModel.onPlaylistClicked(it)
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = Gson().fromJson(
            requireArguments().getString(TRACK_ARG),
            object : TypeToken<Track>() {}.type
        )

        val toolbar = view.findViewById<Toolbar>(R.id.player_toolbar)
        val coverImageView: ImageView = view.findViewById(R.id.cover_image)
        val trackName: TextView = view.findViewById(R.id.track_name)
        val artistName: TextView = view.findViewById(R.id.artist_name)
        val trackDuration: TextView = view.findViewById(R.id.track_duration)
        val album: TextView = view.findViewById(R.id.album)
        val trackAlbum: TextView = view.findViewById(R.id.track_album)
        val trackYear: TextView = view.findViewById(R.id.track_year)
        val trackGenre: TextView = view.findViewById(R.id.track_genre)
        val trackCountry: TextView = view.findViewById(R.id.track_country)
        val addToPlaylist: ImageView = view.findViewById(R.id.add_to_playlist)
        val createPlaylist: TextView = view.findViewById(R.id.create_playlist)
        val bottomSheetContainer: ViewGroup = view.findViewById(R.id.bottom_sheet_container)
        val greyOverlay: View = view.findViewById(R.id.overlay)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> greyOverlay.visibility = View.GONE
                    else -> greyOverlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        currentPlaybackTime = view.findViewById(R.id.current_playback_time)
        favoriteImageView = view.findViewById(R.id.favorite_image)
        playImageView = view.findViewById(R.id.play_image)
        playlistsRecyclerView =
            view.findViewById<RecyclerView>(R.id.playlists_recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = playlistsAdapter
            }
        playImageView.isEnabled = false
        playImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_play_button
            )
        )

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.duration
        trackGenre.text = track.genre
        trackCountry.text = track.country
        trackYear.text = track.releaseYear

        if (track.album == "") {
            trackAlbum.visibility = View.GONE
            album.visibility = View.GONE
        } else {
            trackAlbum.text = track.album
        }

        setFavoriteButton(track.isFavorite)

        val artworkUriHighRes = track.highResArtworkUri
        Glide.with(coverImageView)
            .load(artworkUriHighRes)
            .centerCrop()
            .transform(
                RoundedCorners(
                    coverImageView.resources
                        .getDimensionPixelSize(R.dimen.rounded_corners_album)
                )
            )
            .placeholder(R.drawable.ic_track_placeholder_small)
            .into(coverImageView)

        viewModel.observeState().observe(viewLifecycleOwner) {
            playImageView.isEnabled = it.isPlayButtonEnabled
            currentPlaybackTime.text = it.progress
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
                    greyOverlay.visibility = View.VISIBLE
                    displayPlaylists(it.playlists)
                }

                is PlaylistsInPlayerState.HidePlaylists -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    greyOverlay.visibility = View.GONE
                }
            }
        }
        viewModel.preparePlayer()
        playImageView.setOnClickListener {
            viewModel.onPlayPressed()
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        favoriteImageView.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        addToPlaylist.setOnClickListener {
            viewModel.addToPlaylistClicked()
        }

        createPlaylist.setOnClickListener {
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
                playImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_play_button
                    )
                )

            else ->
                playImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_pause_button
                    )
                )
        }
    }

    private fun setFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            favoriteImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_remove_from_favorites
                )
            )
        } else {
            favoriteImageView.setImageDrawable(
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
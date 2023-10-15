package com.practicum.playlistmaker.playlist_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist_details.view_model.EmptyPlaylistToastState
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistDetails
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistDetailsViewModel
import com.practicum.playlistmaker.playlist_details.view_model.PlaylistMenuState
import com.practicum.playlistmaker.playlist_details.view_model.TracksInPlaylistData
import com.practicum.playlistmaker.playlist_edit.ui.PlaylistEditFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistDetailsBinding
    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder
    private lateinit var onCLickDebounce: (Track) -> Unit
    private var playlistId: Int = 0
    private val viewModel: PlaylistDetailsViewModel by viewModel {
        parametersOf(playlistId)
    }

    private val trackAdapter =
        TrackInPlaylistAdapter(object :
            TrackInPlaylistAdapter.TrackClickListener {
            override fun onTrackLongClickListener(track: Track): Boolean {
                viewModel.onTrackLongClicked(track)
                deleteTrackDialog.show()
                return true
            }

            override fun onTrackClickListener(track: Track) {
                onCLickDebounce(track)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getInt(PLAYLIST_ARG)

        val deletePlaylistDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_MyMaterialAlertDialog)
                .setMessage(buildString {
                    append(resources.getString(R.string.want_to_delete_playlist))
                    append(binding.playlistName.text)
                    append("?")
                })
                .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                }.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    lifecycleScope.launch {
                        viewModel.onPlaylistDeleteConfirmed()
                        findNavController().navigateUp()
                    }
                }

        deleteTrackDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_MyMaterialAlertDialog)
                .setMessage(resources.getString(R.string.delete_track_message))
                .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                }.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    viewModel.onTrackDeleteConfirmed()
                }

        onCLickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_playerFragment,
                PlayerFragment.createArgs(track.toString())
            )
        }

        val tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheetContainer)
        val menuBottomSheetBehavior =
            BottomSheetBehavior.from(binding.menuBottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                binding.playlistDetailsOverlay.visibility = View.GONE
                                tracksBottomSheetBehavior.state =
                                    BottomSheetBehavior.STATE_COLLAPSED
                            }

                            else -> binding.playlistDetailsOverlay.visibility = View.VISIBLE
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }
                })
            }

        viewModel.apply {
            observePlaylistData().observe(viewLifecycleOwner) { data ->
                renderPlaylistData(data)
            }

            observeTracksLiveDate().observe(viewLifecycleOwner) { tracksData ->
                renderTrackData(tracksData)
            }

            observeToastLiveData().observe(viewLifecycleOwner) { state ->
                if (state == EmptyPlaylistToastState.SHOW) {
                    showEmptyPlaylistToast()
                    viewModel.toastWasShown()
                }
            }

            observePlaylistMenuState().observe(viewLifecycleOwner) { state ->
                if (state == PlaylistMenuState.SHOW) {
                    menuBottomSheetBehavior.state =
                        BottomSheetBehavior.STATE_HALF_EXPANDED
                    tracksBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.menuWasShown()
                }
            }
        }

        binding.apply {
            playlistToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            tracksInPlRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
            }

            sharePlaylistImage.setOnClickListener {
                viewModel.onShareClicked(binding.numberOfTracks.text.toString())
            }

            menuImage.setOnClickListener {
                viewModel.onMenuClicked()
            }

            shareMenuItem.setOnClickListener {
                viewModel.onShareClicked(binding.numberOfTracks.text.toString())
            }

            editInfo.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_playlistEditFragment,
                    PlaylistEditFragment.createArgs(playlistId)
                )
            }

            deletePlaylist.setOnClickListener {
                deletePlaylistDialog.setMessage(getDialogueTitle())
                deletePlaylistDialog.show()
            }
        }
    }

    private fun renderPlaylistData(playlistData: PlaylistDetails) {
        with(binding) {
            playlistName.text = playlistData.name
            playlistDescription.text = playlistData.description
            playlistNameSmall.text = playlistData.name
        }
        setCoverImage(playlistData.coverUri)
    }

    private fun renderTrackData(tracksData: TracksInPlaylistData) {
        with(binding) {
            playlistDuration.text =
                resources.getQuantityString(
                    R.plurals.plurals_minutes,
                    tracksData.duration,
                    tracksData.duration
                )
            val numberOfTracksString = resources.getQuantityString(
                R.plurals.plurals_tracks,
                tracksData.numberOfTracks,
                tracksData.numberOfTracks
            )
            numberOfTracks.text = numberOfTracksString
            numberOfTracksSmall.text = numberOfTracksString
        }
        if (tracksData.tracks.isEmpty()) {
            with(binding) {
                tracksInPlRecyclerView.visibility = View.GONE
                emptyPlaylistLayout.visibility = View.VISIBLE
            }
        } else {
            binding.tracksInPlRecyclerView.visibility = View.VISIBLE
            binding.emptyPlaylistLayout.visibility = View.GONE
            trackAdapter.trackList.apply {
                clear()
                addAll(tracksData.tracks)
            }
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun setCoverImage(uri: String) {
        Glide.with(binding.playlistCoverImage)
            .load(uri)
            .placeholder(R.drawable.ic_track_placeholder_large)
            .transform(CenterCrop())
            .into(binding.playlistCoverImage)

        Glide.with(binding.playlistCoverSmall)
            .load(uri)
            .placeholder(R.drawable.ic_track_placeholder_small)
            .transform(CenterCrop())
            .into(binding.playlistCoverSmall)
    }

    private fun showEmptyPlaylistToast() {
        Toast.makeText(
            context,
            resources.getString(R.string.no_tracks_to_share),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getDialogueTitle(): String {
        return "${resources.getString(R.string.delete_playlist_question)} \"${binding.playlistName.text}\"?"
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        fun createArgs(playlistId: Int): Bundle = bundleOf(PLAYLIST_ARG to playlistId)
        private const val PLAYLIST_ARG = "PLAYLIST_ARG"
        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}
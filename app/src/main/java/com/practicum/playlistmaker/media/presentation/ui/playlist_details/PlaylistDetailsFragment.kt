package com.practicum.playlistmaker.media.presentation.ui.playlist_details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.mapper.ParcelablePlaylistMapper
import com.practicum.playlistmaker.media.presentation.models.PlaylistDetailsScreenState
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistDetailsViewModel
import com.practicum.playlistmaker.player.presentation.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistDetailsFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistDetailsBinding
    private val args: PlaylistDetailsFragmentArgs by navArgs()

    private val viewModel: PlaylistDetailsViewModel by viewModel {
        parametersOf(args.playlistId)
    }
    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder
    private lateinit var deletePlaylistDialog: MaterialAlertDialogBuilder

    private lateinit var bottomSheetMenuBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val trackListBottomSheetAdapter = TrackListBottomSheetAdapter(ArrayList()).apply {
        clickListener = TrackListBottomSheetAdapter.TrackClickListener {
            viewModel.showPlayer(it)
        }

        longClickListener = TrackListBottomSheetAdapter.TrackLongClickListener {track ->
            deleteTrackDialog
                .setPositiveButton(R.string.delete_track_dialog_yes_button) { _, _ ->
                    viewModel.onDeleteTrackClick(track)
                }
                .show()
        }
    }

    private val backPressedOnMenuCallback = object: OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (bottomSheetMenuBehavior.state!= BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetMenuBehavior = BottomSheetBehavior.from(binding.llPlaylistBottomSheetMenu)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistDetailsTracksBottomSheet)

        binding.rvPlaylistTrackList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.rvPlaylistTrackList.adapter = trackListBottomSheetAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedOnMenuCallback)

        viewModel.observeTrackList().observe(viewLifecycleOwner) { trackList ->
            renderTrackList(trackList)
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.getShowPlayerTrigger().observe(viewLifecycleOwner) {
            showPlayerActivity(it)
        }

        viewModel.getDeletePlaylistTrigger().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.getShowPlaylistEditTrigger().observe(viewLifecycleOwner) { playlist ->
            showPlaylistEditFragment(playlist)
        }

        binding.btPlaylistDetailsBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btPlaylistDetailsSharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistBottomSheetMenu.btPlaylistDetailsShare.setOnClickListener {
            showBottomMenu(isVisible = false)
            sharePlaylist()
        }

        binding.playlistBottomSheetMenu.btPlaylistDetailsDelete.setOnClickListener {
            showBottomMenu(isVisible = false)
            deletePlaylistDialog.show()
        }

        binding.btPlaylistDetailsMenu.setOnClickListener {
            showBottomMenu(isVisible = true)
        }

        binding.playlistBottomSheetMenu.btPlaylistDetailsEdit.setOnClickListener {
            showBottomMenu(isVisible = false)
            viewModel.showPlaylistEdit()
        }

        bottomSheetMenuBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        backPressedOnMenuCallback.isEnabled = false
                        binding.playlistOverlay.visibility = View.GONE
                    }
                    else -> {
                        binding.playlistOverlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        init()
    }

    private fun init() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        showBottomMenu(isVisible = false)
        initDialogs()
    }

    private fun initDialogs() {
        deleteTrackDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track_dialog_title))
            .setMessage(R.string.delete_track_dialog_message)
            .setNegativeButton(R.string.delete_track_dialog_cancel_button) {_, _ -> }
            .setPositiveButton(R.string.delete_track_dialog_yes_button) { _, _ -> }

        deletePlaylistDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist_dialog_title))
            .setMessage(R.string.delete_playlist_dialog_message)
            .setNegativeButton(R.string.delete_playlist_dialog_cancel_button) {_, _ -> }
            .setPositiveButton(R.string.delete_playlist_dialog_yes_button) { _, _ ->
                viewModel.observeState().removeObservers(viewLifecycleOwner)
                viewModel.onDeletePlaylist()
            }
    }

    private fun renderState(state: PlaylistDetailsScreenState) {
        when (state) {
            is PlaylistDetailsScreenState.Content -> setContent(state.data)
            is PlaylistDetailsScreenState.Error -> processError(state.message)
            else -> Unit
        }
    }

    private fun renderTrackList(trackList: List<Track>) {
        binding.playlistBottomSheetMenu.playlistView.playlistListItemTrackCount.text =
            viewModel.getTrackCountStatistics()
        binding.tvPlaylistDetailsStatistics.text = getString(
            R.string.playlist_statistics,
            viewModel.getPlaylistTimeStatistics(),
            viewModel.getTrackCountStatistics()
        )
        trackListBottomSheetAdapter.addItems(trackList)

        binding.tvPlaylistTrackEmpty.isVisible = trackList.isEmpty()
        binding.rvPlaylistTrackList.isVisible = trackList.isNotEmpty()
    }

    private fun setContent(playlist: Playlist) {
        binding.tvPlaylistDetailsName.text = playlist.name.orEmpty()
        binding.tvPlaylistDetailsDescription.text = playlist.description.orEmpty()
        binding.playlistDetailsImageView.setImageResource(R.drawable.ic_playlist_large)
        if (!playlist.filePath.isNullOrEmpty()) {
            val file = playlist.filePath?.let { File(viewModel.getImageDirectory(), it) }
            binding.playlistDetailsImageView.setImageURI(Uri.fromFile(file))
            binding.playlistDetailsImageView.clipToOutline = true

            binding.playlistBottomSheetMenu.playlistView.playlistListItemImage.setImageURI(Uri.fromFile(file))
            binding.playlistBottomSheetMenu.playlistView.playlistListItemImage.clipToOutline = true
        }
        binding.playlistBottomSheetMenu.playlistView.playlistListItemName.text =
            playlist.name.orEmpty()
        binding.playlistDetailsTracksBottomSheet.doOnLayout {
            bottomSheetBehavior.peekHeight = getPeekHeight()
        }
    }

    private fun processError(message: String) {
        showMessage(message)
        findNavController().navigateUp()
    }

    private fun getPeekHeight(): Int {
        return binding.root.measuredHeight - binding.playlistDetailsConstraintLayout.measuredHeight - resources.getDimensionPixelSize(
            R.dimen.large_margin
        )
    }

    private fun showPlayerActivity(track: Track) {
        findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerActivity,
            PlayerActivity.createArgs(track))
    }

    private fun sharePlaylist() {
        if (!viewModel.onSharePlaylist()) {
            showMessage(getString(R.string.share_playlist_empty_message))
        }
    }

    private fun showBottomMenu(isVisible: Boolean) {
        backPressedOnMenuCallback.isEnabled = isVisible
        if (isVisible) {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showPlaylistEditFragment(playlist: Playlist) {
        val action = PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToPlaylistEditFragment(
            ParcelablePlaylistMapper.map(playlist)
        )
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
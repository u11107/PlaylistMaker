package com.practicum.playlistmaker.media.presentation.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.presentation.models.PlaylistsScreenState
import com.practicum.playlistmaker.media.presentation.ui.MediaFragmentDirections
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private val playlistsAdapter = PlaylistsAdapter(ArrayList()).apply {
        clickListener = PlaylistsAdapter.PlaylistClickListener { playlist ->
            playlistsViewModel.showPlaylistDetails(playlist.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycle.addObserver(playlistsViewModel)

        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsGridView.rvPlaylistGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsGridView.rvPlaylistGrid.adapter = playlistsAdapter

        playlistsViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        playlistsViewModel.getShowPlaylistDetailsTrigger().observe(viewLifecycleOwner) {playlistId ->
            showPlaylistDetails(playlistId)
        }

        binding.playlistsEmpty.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment)
        }

        binding.playlistsGridView.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment)
        }
    }

    private fun render(state: PlaylistsScreenState) {
        binding.playlistsEmpty.root.isVisible = state is PlaylistsScreenState.Empty
        binding.progressBar.isVisible = state is PlaylistsScreenState.Loading
        binding.playlistsGridView.root.isVisible = state is PlaylistsScreenState.Content

        when (state) {
            is PlaylistsScreenState.Loading, PlaylistsScreenState.Empty -> Unit
            is PlaylistsScreenState.Content -> playlistsAdapter.addItems(state.playlists)
        }
    }

    private fun showPlaylistDetails(playlistId: Long) {
        val action = MediaFragmentDirections.actionMediaFragmentToPlaylistDetailsFragment(
            playlistId
        )
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
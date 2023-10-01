package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.media.ui.model.FavouriteTracksState
import com.example.playlistmaker.media.ui.viewModel.FavouriteTracksViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouriteTracksFragment : Fragment() {

    private var _binding: FragmentFavoritesTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavouriteTracksViewModel>()

    private val favoritesTracksAdapter = TrackAdapter {
        showPlayer(track = it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteTracksRecycler.adapter = favoritesTracksAdapter

        viewModel.getFavouriteTracks()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavouriteTracksState) {
        when (state) {
            is FavouriteTracksState.Content -> {
                favoritesTracksAdapter.clearTracks()
                favoritesTracksAdapter.tracks = state.tracks as MutableList<Track>
                binding.favouriteTracksRecycler.visibility = View.VISIBLE
                binding.nothingFound.visibility = View.GONE
            }

            is FavouriteTracksState.Empty -> {
                binding.favouriteTracksRecycler.visibility = View.GONE
                binding.nothingFound.visibility = View.VISIBLE
            }
        }
    }

    private fun showPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.action_mediaFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTracks()
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment().apply {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
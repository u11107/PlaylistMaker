package com.practicum.playlistmaker.media.presentation.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.media.presentation.models.FavouritesScreenState
import com.practicum.playlistmaker.media.presentation.view_model.FavouritesViewModel
import com.practicum.playlistmaker.player.presentation.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment: Fragment() {
    private lateinit var binding: FragmentFavouritesBinding

    private val viewModel: FavouritesViewModel by viewModel()

    private val favouritesAdapter = FavouritesAdapter(ArrayList()).apply {
        clickListener = FavouritesAdapter.TrackClickListener {
            viewModel.showPlayer(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycle.addObserver(viewModel)

        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouriteTrackList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favouriteTrackList.adapter = favouritesAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getShowPlayerTrigger().observe(viewLifecycleOwner) {
            showPlayerActivity(it)
        }
    }

    private fun render(state: FavouritesScreenState) {
        binding.favouritesEmpty.root.isVisible = state is FavouritesScreenState.Empty
        binding.favouriteTrackList.isVisible = state is FavouritesScreenState.Content
        binding.progressBar.isVisible = state is FavouritesScreenState.Loading
        when (state) {
            is FavouritesScreenState.Loading, FavouritesScreenState.Empty -> Unit
            is FavouritesScreenState.Content -> favouritesAdapter.addItems(state.tracks)
        }
    }

    private fun showPlayerActivity(track: Track) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerActivity,
            PlayerActivity.createArgs(track))
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }
}
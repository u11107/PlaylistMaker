package com.practicum.playlistmaker.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorites.view_model.FavoritesState
import com.practicum.playlistmaker.favorites.view_model.FavoritesViewModel
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.TrackViewHolder
import com.practicum.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var emptyFavoritesLayout: ViewGroup
    private val favoritesAdapter =
        TrackAdapter<TrackViewHolder>(object : TrackAdapter.TrackClickListener {
            override fun onTrackClickListener(track: Track) {
                onClickDebounce(track)
            }
        })

    private lateinit var onClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view)
        favoritesRecyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        emptyFavoritesLayout = view.findViewById(R.id.empty_favorites_layout)

        viewModel.observeFavoritesState().observe(viewLifecycleOwner) {
            render(it)
        }

        onClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_playerFragment,
                PlayerFragment.createArgs(track.toString())
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.displayState()
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.DisplayFavorites -> displayFavorites(state.tracks)
            is FavoritesState.EmptyFavorites -> displayPlaceholer()
        }
    }

    private fun displayFavorites(tracks: List<Track>) {
        emptyFavoritesLayout.visibility = View.GONE
        favoritesRecyclerView.visibility = View.VISIBLE
        favoritesAdapter.trackList.clear()
        favoritesAdapter.trackList.addAll(tracks)
        favoritesAdapter.notifyDataSetChanged()
    }

    private fun displayPlaceholer() {
        emptyFavoritesLayout.visibility = View.VISIBLE
        favoritesRecyclerView.visibility = View.GONE
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}
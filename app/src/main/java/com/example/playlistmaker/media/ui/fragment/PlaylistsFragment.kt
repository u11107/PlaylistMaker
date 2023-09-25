package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.media.ui.viewModel.FavouriteTracksViewModel
import com.example.playlistmaker.media.ui.viewModel.PlayListViewModel
import com.example.playlistmaker.media.ui.viewModel.PlaylistsScreenState
import com.example.playlistmaker.player.ui.ItemDecorator
import com.example.playlistmaker.player.ui.adapter.PlaylistsAdapter
import com.example.playlistmaker.util.ViewObjects
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlayListViewModel>()

    private val playlistsAdapter = PlaylistsAdapter(viewObject = ViewObjects.Horizontal)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.newPlayListFragment)
        }

        viewModel.fillData()

        binding.playlistsGrid.adapter = playlistsAdapter

        binding.playlistsGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsGrid.setHasFixedSize(true)
        binding.playlistsGrid.addItemDecoration(ItemDecorator(40, 0))

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render()
        }

    }

    private fun render() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Filled -> {
                    showPlaylists(state.playlists)
                }

                is PlaylistsScreenState.Empty -> {
                    binding.playlistsGrid.visibility = View.GONE
                    binding.nothingFound.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        playlistsAdapter.playlists = playlists as ArrayList<Playlist>
        binding.playlistsGrid.visibility = View.VISIBLE
        binding.nothingFound.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
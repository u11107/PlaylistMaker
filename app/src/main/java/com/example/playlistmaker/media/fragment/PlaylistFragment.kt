package com.example.playlistmaker.media.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.viewmodel.FavoritesTracksViewModel
import com.example.playlistmaker.media.viewmodel.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding

    private val playListViewModel: PlayListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() =
            PlaylistFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
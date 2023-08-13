package com.example.playlistmaker.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
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
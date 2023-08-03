package com.example.playlistmaker.media.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.media.viewmodel.FavoritesTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class FavoritesTracksFragment() : Fragment() {

    private var _binding : FragmentFavoritesTracksBinding? = null
    private val binding get() = _binding!!

    private val fragmentViewModel: FavoritesTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesTracksBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        fun newInstance() =
            FavoritesTracksFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
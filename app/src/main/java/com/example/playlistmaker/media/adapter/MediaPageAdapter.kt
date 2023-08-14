package com.example.playlistmaker.media.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media.fragment.FavoritesTracksFragment
import com.example.playlistmaker.media.fragment.PlaylistFragment

class MediaPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesTracksFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}



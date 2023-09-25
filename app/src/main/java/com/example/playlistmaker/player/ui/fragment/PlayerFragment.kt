package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.ui.model.Playlist
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.adapter.PlaylistsAdapter
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.TRACK
import com.example.playlistmaker.util.ViewObjects
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerFragment : Fragment() {
    private var _playerBinding: FragmentPlayerBinding? = null
    private val playerBinding get() = _playerBinding!!

    private val viewModel by viewModel<PlayerViewModel>()

    private val bottomSheetPlaylistsAdapter = PlaylistsAdapter(viewObject = ViewObjects.Vertical)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _playerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        initAdapters()

        val track = requireArguments().getSerializable(TRACK) as Track
        val bottomSheetBehavior =
            BottomSheetBehavior.from(playerBinding.bottomSheetLinear).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        playerBinding.overlay.visibility = View.GONE
                    }

                    else -> {
                        playerBinding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        track.previewUrl?.let { viewModel.prepare(it) }

        track.trackId.let { viewModel.checkIsFavourite(it) }

        playerBinding.likeButton.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }

        bottomSheetPlaylistsAdapter.onPlayListClicked = {
            viewModel.addTrackToPlayList(track, it)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        playerBinding.addButton.setOnClickListener {
            viewModel.fillData()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        showTrack(track)
    }

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
        playerBinding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlayListFragment)
        }
    }

    private fun initAdapters() {
        playerBinding.playlistsBottomSheetRecyclerview.adapter = bottomSheetPlaylistsAdapter
    }

    private fun initObservers() {
        viewModel.observeIsFavourite().observe(viewLifecycleOwner) { isFavorite ->
            playerBinding.likeButton.setImageResource(
                if (isFavorite) R.drawable.ic_like_button_favourite else R.drawable.like_button
            )
        }
        viewModel.observeTime().observe(viewLifecycleOwner) {
            playerBinding.time.text = it
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.time.text = getString(R.string.track_time)
                setPlayIcon()
            }
        }
        viewModel.isAlreadyInPlaylist.observe(viewLifecycleOwner) {
            val message =
                if (it.second) "Добавлено в плейлист ${it.first}" else "Трек уже добавлен в плейлист ${it.first}"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        viewModel.playlists.observe(viewLifecycleOwner) {
            bottomSheetPlaylistsAdapter.playlists = it as ArrayList<Playlist>
            viewModel.fillData()
        }
    }

    private fun showTrack(track: Track) {
        playerBinding.apply {
            Glide
                .with(mediaTrackImage)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
                .into(mediaTrackImage)
            trackName.text = track.trackName
            artistName.text = track.artistName
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            val date =
                track.releaseDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                        it
                    )
                }
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                releaseDate.text = formattedDatesString
            }
            if (track.collectionName.isNotEmpty()) {
                collectionName.text = track.collectionName
            } else {
                collectionName.visibility = View.GONE
                trackAlbum.visibility = View.GONE
            }
        }
    }

    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                viewModel.play()
                setPauseIcon()
            }

            PlayerState.STATE_PLAYING -> {
                viewModel.pause()
                setPlayIcon()
            }
        }
    }

    private fun setPlayIcon() {
        playerBinding.playButton.setImageResource(R.drawable.play)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.pause)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
        _playerBinding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        fun createArgs(track: Track): Bundle {
            return bundleOf(TRACK to track)
        }
    }
}
package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private lateinit var playerBinding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerBinding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        initListeners()

        val track = intent.getSerializableExtra(TRACK) as Track
        track.previewUrl?.let { viewModel.prepare(it) }

        viewModel.observeState().observe(this) { state ->
            playerBinding.btPlay.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.durationTrackTv.text = getString(R.string.time_start_position)
                setPlayIcon()
            }
        }

        viewModel.checkIsFavourite(track.trackId)

        playerBinding.btLike.setOnClickListener {
            viewModel.onFavouriteClicked(track)
        }

        viewModel.observeTime().observe(this) {
            playerBinding.durationTrackTv.text = it
        }

        viewModel.observeIsFavourite().observe(this) {
                isFavorite ->
            playerBinding.btLike.setImageResource(
                if (isFavorite) R.drawable.ic_like_button_favourite else R.drawable.like_button
            )
        }

        showTrack(track)
    }

    private fun showTrack(track: Track) {
        playerBinding.apply {
            Glide
                .with(imagePicture)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_8)))
                .into(imagePicture)

            tittleTrackName.text = track.trackName
            tittleTrackArtist.text = track.artistName
            trackGenre.text = track.primaryGenreName
            trackCountry.text = track.country

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
                trackYear.text = formattedDatesString
            }

            if (track.collectionName.isNotEmpty()) {
                trackAlb.text = track.collectionName
            } else {
                trackAlb.visibility = View.GONE
                albumTrack.visibility = View.GONE
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

    private fun initListeners() {
        playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }

    }

    private fun setPlayIcon() {
        playerBinding.btPlay.setImageResource(R.drawable.play)
    }

    private fun setPauseIcon() {
        playerBinding.btPlay.setImageResource(R.drawable.pause)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}
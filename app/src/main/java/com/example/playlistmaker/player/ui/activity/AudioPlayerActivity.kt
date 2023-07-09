package com.example.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.util.App.Companion.TRACK
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var playerVIewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener {
            finish()
        }

        playerVIewModel = ViewModelProvider(
            this, PlayerViewModel
                .getViewModelFactory()
        )[PlayerViewModel::class.java]


        val track =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(TRACK, Track::class.java)
            } else {
                intent.getParcelableExtra(TRACK)
            } as Track
        initViews(track)
        playerVIewModel.prepare(track.previewUrl)
        playerVIewModel.observeState().observe(this) { state ->
            binding.btPlay.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.durationTrackTv.text = getString(R.string.time_start_position)
                pausePlayer()
            }
        }
        playerVIewModel.observeTime().observe(this) {
            binding.durationTrackTv.text = it
        }
    }

    private fun initViews(track: Track) = with(binding) {
        tittleTrackName.text = track.trackName
        tittleTrackArtist.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis)
        if (track.collectionName.isNullOrEmpty()) {
            trackAlb.visibility = View.GONE
            albumTrack.visibility = View.GONE
        } else {
            trackAlb.text = track.collectionName
        }

        trackYear.text = track.getReleaseDateOnlyYear()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
        Glide.with(imagePicture)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_8)))
            .into(imagePicture)
    }

    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_COMPLETE -> {
                startPlayer()
            }

            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
        }
    }

    private fun startPlayer() {
        binding.btPlay.setImageResource(R.drawable.pause)
        playerVIewModel.play()
    }

    private fun pausePlayer() {
        binding.btPlay.setImageResource(R.drawable.play)
        playerVIewModel.pause()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerVIewModel.release()
    }

}
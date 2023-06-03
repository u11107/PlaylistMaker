package com.example.playlistmaker

import android.app.usage.NetworkStats.Bucket.STATE_DEFAULT
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.Companion.TRACK
import com.example.playlistmaker.App.Companion.formatTime
import com.example.playlistmaker.App.Companion.themeDark
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_MILLIS = 1000L
    }

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable by lazy {
        Runnable {
            binding.durationTrackTv.text = formatTime(mediaPlayer.currentPosition.toLong())
            handler.postDelayed(runnable, DELAY_MILLIS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener {
            finish()
        }

        val json = intent.getStringExtra(TRACK)!!

        val track = Gson().fromJson(json, Track::class.java)
        goToPlayer(track)
        preparePlayer(track.previewUrl)

        binding.btPlay.setOnClickListener {
            playbackControl()
        }
        binding.durationTrackTv.setText(R.string.time_start_position)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    private fun goToPlayer(track: Track) = with(binding) {
        tittleTrackName.text = track.trackName
        tittleTrackArtist.text = track.artistName
        trackTime.text = formatTime(track.trackTimeMillis)
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
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_album)))
            .into(imagePicture)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.btPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.btPlay.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            binding.durationTrackTv.setText(R.string.time_start_position)
            handler.removeCallbacks(runnable)

        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.btPlay.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.btPlay.setImageResource(R.drawable.play)
        handler.removeCallbacks(runnable)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}
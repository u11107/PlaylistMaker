package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.Companion.TRACK
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>(TRACK)!!
        fillingThePlayer(track)
    }

    private fun fillingThePlayer(track: Track) = with(binding) {
        tittleTrackName.text = track.trackName
        tittleTrackArtist.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        if (track.collectionName.isEmpty()) {
            trackAlb.visibility = View.GONE
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
}
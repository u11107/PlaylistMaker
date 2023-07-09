package com.example.playlistmaker.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
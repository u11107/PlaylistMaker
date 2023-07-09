package com.example.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.media.MediaActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.setting.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.butSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        binding.butMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }
        binding. butSetting.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
    }


}
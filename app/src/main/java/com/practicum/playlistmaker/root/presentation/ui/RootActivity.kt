package com.practicum.playlistmaker.root.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistFragment, R.id.playlistEditFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.playlistDetailsFragment -> showBottomNavigationBar(isVisible = false)
                else -> showBottomNavigationBar(isVisible = true)
            }

        }
    }

    private fun showBottomNavigationBar(isVisible: Boolean) {
        val viewVisibility = if (isVisible) View.VISIBLE else View.GONE
        binding.bottomNavigationView.visibility = viewVisibility
        binding.dividerView.visibility = viewVisibility
    }
}
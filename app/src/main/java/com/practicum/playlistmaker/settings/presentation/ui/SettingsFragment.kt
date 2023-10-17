package com.practicum.playlistmaker.settings.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDarkTheme().observe(viewLifecycleOwner) {
            binding.scNightTheme.isChecked = viewModel.observeDarkTheme().value == true
        }

        binding.scNightTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.btShareApp.setOnClickListener {
            viewModel.onShareAppBtnClick()
        }

        binding.btService.setOnClickListener {
            viewModel.onServiceBtnClick()
        }

        binding.btTermsOfUse.setOnClickListener {
            viewModel.onTermsOfUseBtnClick()
        }
    }
}
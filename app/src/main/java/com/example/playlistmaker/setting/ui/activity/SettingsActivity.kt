package com.example.playlistmaker.setting.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingBinding
import com.example.playlistmaker.setting.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

        binding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        viewModel.themeSettingsState.observe(this) { themeSettings ->
            binding.switchTheme.isChecked = themeSettings.darkTheme
        }
    }

    private fun initListeners() {
        binding.shareTextView.setOnClickListener {
            viewModel.shareApp()
        }
        binding.callTextView.setOnClickListener {
            viewModel.supportEmail()
        }
        binding.agreementTextView.setOnClickListener {
            viewModel.openAgreement()
        }
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }
}

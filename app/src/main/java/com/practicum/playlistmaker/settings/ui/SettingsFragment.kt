package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shareAppTextView = view.findViewById<TextView>(R.id.share_app)
        val supportTextView = view.findViewById<TextView>(R.id.support)
        val userAgreementTextView = view.findViewById<TextView>(R.id.user_agreement)
        val themeSwitch = view.findViewById<SwitchMaterial>(R.id.switch_dark_theme)

        themeSwitch.isChecked = viewModel.darkThemeIsEnabled()

        shareAppTextView.setOnClickListener {
            viewModel.onShareAppPressed()
        }

        supportTextView.setOnClickListener {
            viewModel.onSupportPressed()
        }

        userAgreementTextView.setOnClickListener {
            viewModel.onUserAgreementPressed()
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onThemeSwitched(isChecked)
        }
    }
}
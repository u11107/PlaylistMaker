package com.example.playlistmaker.setting.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingBinding
import com.example.playlistmaker.setting.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingFragment : Fragment() {

    private lateinit var binding:FragmentSettingBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        viewModel.themeSettingsState.observe(viewLifecycleOwner) { themeSettings ->
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
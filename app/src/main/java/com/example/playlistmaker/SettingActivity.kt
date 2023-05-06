package com.example.playlistmaker

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingBinding


class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonBack.setOnClickListener {
            finish()
        }


        binding.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = ACTION_SEND
                putExtra(EXTRA_TEXT, getString(R.string.url_address))
                type = "text/plain"
            }
            val shareIntent = createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.sendSupport.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
                putExtra(EXTRA_SUBJECT, getString(R.string.themeMessage))
                putExtra(EXTRA_TEXT, getString(R.string.message))
            }
            val sendEmail = createChooser(sendIntent, null)
            startActivity(sendEmail)
        }

        binding.agreementUser.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = ACTION_VIEW
                data = Uri.parse(getString(R.string.url_address_oferta))
            }
            val sendEmail = createChooser(sendIntent, null)
            startActivity(sendEmail)
        }

        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        if ((applicationContext as App).darkTheme) {
            binding.themeSwitch.isChecked = true;
        }
    }
}

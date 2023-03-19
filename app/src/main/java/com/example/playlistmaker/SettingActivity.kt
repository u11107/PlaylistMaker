package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val butBack = findViewById<Button>(R.id.button_setting_back)
        val shareApp = findViewById<TextView>(R.id.share)
        val sendSupport = findViewById<TextView>(R.id.sendSupport)
        val agreementUser = findViewById<TextView>(R.id.agreementUser)

        butBack.setOnClickListener {
            finish()
        }

        shareApp.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = ACTION_SEND
                putExtra(EXTRA_TEXT, getString(R.string.url_address))
                type = "text/plain"
            }
            val shareIntent = createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        sendSupport.setOnClickListener {
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

        agreementUser.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = ACTION_VIEW
                data = Uri.parse(getString(R.string.url_address_oferta))
            }
            val sendEmail = createChooser(sendIntent, null)
            startActivity(sendEmail)
        }
    }
}

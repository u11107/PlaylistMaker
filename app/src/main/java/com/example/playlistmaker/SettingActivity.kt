package com.example.playlistmaker

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
            val message = "https://practicum.yandex.ru/android-developer/"
            val sendIntent: Intent = Intent().apply {
                action = ACTION_SEND
                putExtra(EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        sendSupport.setOnClickListener {
            val email = arrayOf("ren84@yandex.ru")
            val themeMessage = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val sendIntent: Intent = Intent().apply {
                action = ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(EXTRA_EMAIL, email)
                putExtra(EXTRA_SUBJECT, themeMessage)
                putExtra(EXTRA_TEXT, message)
            }
            val sendEmail = createChooser(sendIntent, null)
            startActivity(sendEmail)
        }

        agreementUser.setOnClickListener {
            val link = "https://yandex.ru/legal/practicum_offer/"
            val sendIntent: Intent = Intent().apply {
                action = ACTION_VIEW
                data = Uri.parse(link)
            }
            val sendEmail = createChooser(sendIntent, null)
            startActivity(sendEmail)
        }
    }
}

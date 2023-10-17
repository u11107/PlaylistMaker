package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.models.Email
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.UrlData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(url: UrlData) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getUrlString(url))
            context.startActivity(
                Intent.createChooser(shareIntent, getUrlTitleString(url))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (_: Exception) {
        }
    }

    override fun openLink(url: UrlData) {
        try {
            val browseIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getUrlString(url)))
            browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browseIntent)
        } catch (_: Exception) {
        }
    }

    override fun openEmail(email: EmailData) {
        val emailInfo = getEmail(email)
        try {
            val serviceIntent = Intent(Intent.ACTION_SENDTO)
            serviceIntent.data = Uri.parse(context.getString(R.string.mailto))
            serviceIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(emailInfo.address)
            )
            serviceIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                emailInfo.subject
            )
            serviceIntent.putExtra(Intent.EXTRA_TEXT, emailInfo.body)
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(serviceIntent)
        } catch(_: Exception) {
        }
    }

    private fun getUrlString(url: UrlData): String {
        return when (url) {
            UrlData.SHARE_APP_URL -> context.getString(R.string.share_url)
            UrlData.TERMS_URL -> context.getString(R.string.terms_of_use_url)
        }
    }

    private fun getUrlTitleString(url: UrlData): String {
        return when (url) {
            UrlData.SHARE_APP_URL -> context.getString(R.string.tune_share_app)
            UrlData.TERMS_URL -> ""
        }
    }

    private fun getEmail(email: EmailData): Email {
        return when (email) {
            EmailData.SUPPORT_EMAIL -> Email(
                context.getString(R.string.service_email),
                context.getString(R.string.service_email_subject),
                context.getString(R.string.service_email_body)
            )
        }
    }
}
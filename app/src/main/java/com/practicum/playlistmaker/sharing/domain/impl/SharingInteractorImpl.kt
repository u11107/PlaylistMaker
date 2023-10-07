package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.utils.ResourceProvider

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink() = resourceProvider.getString(R.string.practicum_android_link)

    private fun getSupportEmailData() = EmailData(
        email = resourceProvider.getString(R.string.feedback_addressee_mail),
        subject = resourceProvider.getString(R.string.feedback_subject),
        textMessage = resourceProvider.getString(R.string.feedback_message_text)
    )

    private fun getTermsLink() = resourceProvider.getString(R.string.practicum_term_link)
}
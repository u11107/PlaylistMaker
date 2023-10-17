package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.UrlData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(UrlData.SHARE_APP_URL)
    }

    override fun openTermsOfUse() {
        externalNavigator.openLink(UrlData.TERMS_URL)
    }

    override fun openSupport() {
        externalNavigator.openEmail(EmailData.SUPPORT_EMAIL)
    }
}
package com.practicum.playlistmaker.sharing.data

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareString(stringToShare: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}
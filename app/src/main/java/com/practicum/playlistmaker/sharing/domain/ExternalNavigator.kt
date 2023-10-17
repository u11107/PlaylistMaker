package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.models.UrlData

interface ExternalNavigator {
    fun shareLink(url: UrlData)
    fun openLink(url: UrlData)
    fun openEmail(email: EmailData)
}
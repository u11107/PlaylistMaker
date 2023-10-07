package com.practicum.playlistmaker.sharing.domain.model

data class EmailData(
    val email: String,
    val subject: String,
    val textMessage: String
)
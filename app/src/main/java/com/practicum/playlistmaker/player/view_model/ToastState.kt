package com.practicum.playlistmaker.player.view_model

sealed interface ToastState {
    object None : ToastState
    data class Show(val additionalMessage: String) : ToastState
}
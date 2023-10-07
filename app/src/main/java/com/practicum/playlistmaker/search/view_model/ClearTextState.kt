package com.practicum.playlistmaker.search.view_model

sealed interface ClearTextState {
    object None : ClearTextState
    object ClearText : ClearTextState
}
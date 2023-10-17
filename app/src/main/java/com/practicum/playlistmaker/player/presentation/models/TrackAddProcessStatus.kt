package com.practicum.playlistmaker.player.presentation.models

sealed interface TrackAddProcessStatus {
    object None: TrackAddProcessStatus
    data class Error(val name: String?): TrackAddProcessStatus
    data class Added(val name: String?): TrackAddProcessStatus
    data class Exist(val name: String?): TrackAddProcessStatus
}
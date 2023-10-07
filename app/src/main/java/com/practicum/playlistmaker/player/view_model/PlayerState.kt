package com.practicum.playlistmaker.player.view_model

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val buttonText: String,
    val progress: String
) {
    class DefaultState : PlayerState(false, PLAY_BUTTON, ZERO_TIMER)
    class PreparedState : PlayerState(true, PLAY_BUTTON, ZERO_TIMER)
    class PlayingState(progress: String) : PlayerState(true, PAUSE_BUTTON, progress)
    class PauseState(progress: String) : PlayerState(true, PLAY_BUTTON, progress)

    companion object {
        const val ZERO_TIMER = "00:00"
        const val PLAY_BUTTON = "PLAY_BUTTON"
        const val PAUSE_BUTTON = "PAUSE_BUTTON"
    }
}
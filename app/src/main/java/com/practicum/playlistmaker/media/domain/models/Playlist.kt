package com.practicum.playlistmaker.media.domain.models

data class Playlist(
    var id: Long = 0,
    var name: String? = null,
    var description: String? = null,
    var filePath: String? = null,
    var trackList: ArrayList<Long> = ArrayList(),
    var trackCount: Int = 0,
)

package com.practicum.playlistmaker.playlists_creation.domain.model

data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val coverUri: String,
    val tracks: ArrayList<Int>,
    val numberOfTracks: Int
) {
    constructor(
        name: String,
        description: String,
        coverPath: String,
    ) : this(0, name, description, coverPath, ArrayList<Int>(), 0)
}
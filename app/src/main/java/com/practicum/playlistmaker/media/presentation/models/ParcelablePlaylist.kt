package com.practicum.playlistmaker.media.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ParcelablePlaylist(
    var id: Long = 0,
    var name: String? = null,
    var description: String? = null,
    var filePath: String? = null,
    var trackList: ArrayList<Long> = ArrayList(),
    var trackCount: Int = 0,
) : Parcelable

package com.practicum.playlistmaker.player.presentation.models

import android.os.Parcel
import android.os.Parcelable

data class ParcelableTrack(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val albumName: String?,
    val releaseYear: Int?,
    val genreName: String?,
    val country: String?,
    val previewUrl: String?,
    val isFavourite: Boolean,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
    )

    constructor() : this(null, null, null, 0, null, null, null, null, null, null, false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(trackId!!)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(albumName)
        parcel.writeInt(releaseYear ?: 0)
        parcel.writeString(genreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
        parcel.writeBoolean(isFavourite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableTrack> {
        override fun createFromParcel(parcel: Parcel): ParcelableTrack {
            return ParcelableTrack(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableTrack?> {
            return arrayOfNulls(size)
        }
    }
}

package com.practicum.playlistmaker.utils

interface ResourceProvider {
    fun getString(resId: Int): String
}
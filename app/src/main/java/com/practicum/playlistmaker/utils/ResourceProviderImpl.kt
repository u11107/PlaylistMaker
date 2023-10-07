package com.practicum.playlistmaker.utils

import android.content.Context

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int) = context.getString(resId)
}
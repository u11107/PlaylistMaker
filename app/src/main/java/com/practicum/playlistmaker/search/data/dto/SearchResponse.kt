package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(@SerializedName("results") var trackList: List<TrackDto>) : Response()
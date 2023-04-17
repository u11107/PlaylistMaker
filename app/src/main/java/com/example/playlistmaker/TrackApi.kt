package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String):Call<TrackResponse>
}
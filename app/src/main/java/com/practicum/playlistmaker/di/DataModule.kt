package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.player.data.PlayerImpl
import com.practicum.playlistmaker.player.domain.api.Player
import com.practicum.playlistmaker.playlists_creation.data.local_files.PrivateStorage
import com.practicum.playlistmaker.playlists_creation.data.local_files.PrivateStorageImpl
import com.practicum.playlistmaker.search.data.LocalStorage
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.network.ItunesService
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.sharedprefs.LocalStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<Player> {
        PlayerImpl(get())
    }

    single<LocalStorage> {
        LocalStorageImpl(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single {
        Gson()
    }

    single {
        MediaPlayer()
    }

    factory { (query: String) ->
        SearchRequest(query)
    }

    single<ItunesService> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("shared_preference", Context.MODE_PRIVATE)
    }

    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    single<PrivateStorage> {
        PrivateStorageImpl(androidContext())
    }

}
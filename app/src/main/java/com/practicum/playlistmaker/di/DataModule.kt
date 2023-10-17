package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Environment
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.media.data.AppDatabase
import com.practicum.playlistmaker.media.data.impl.ExternalNavigatorMediaImpl
import com.practicum.playlistmaker.media.domain.api.ExternalNavigatorMedia
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.utils.QUALIFIER_IMAGE_DIRECTORY
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://itunes.apple.com")
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single {
        androidContext()
            .getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single(named(QUALIFIER_IMAGE_DIRECTORY)) {
        File(
            androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlistImage"
        ).apply { if (!exists()) mkdirs() }
    }

    single<ExternalNavigatorMedia> {
        ExternalNavigatorMediaImpl(androidContext())
    }

    factory { Gson() }

    factory { MediaPlayer() }
}
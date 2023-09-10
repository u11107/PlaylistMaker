package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dbModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
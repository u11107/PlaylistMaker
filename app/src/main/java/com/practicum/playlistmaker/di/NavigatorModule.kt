package com.practicum.playlistmaker.di

import android.content.Intent
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val navigatorModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { (cls: Class<*>) ->
        Intent(androidContext(), cls)
    }
}
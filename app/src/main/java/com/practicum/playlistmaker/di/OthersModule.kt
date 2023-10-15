package com.practicum.playlistmaker.di

import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.utils.ResourceProvider
import com.practicum.playlistmaker.utils.ResourceProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val othersModule = module {

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }

    single {
        PermissionRequester.instance()
    }
}
package com.practicum.playlistmaker.di

import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.utils.ResourceProvider
import com.practicum.playlistmaker.utils.ResourceProviderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val othersModule = module {
    singleOf(::ResourceProviderImpl) bind ResourceProvider::class

    single {
        PermissionRequester.instance()
    }
}
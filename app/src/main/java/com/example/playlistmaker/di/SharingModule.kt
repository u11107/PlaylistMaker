package com.example.playlistmaker.di

import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.repository.ExternalNavigator
import org.koin.dsl.module

val sharingModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }
}
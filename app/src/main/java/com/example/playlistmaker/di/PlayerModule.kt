package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.interactor.PlayerInteractor
import com.example.playlistmaker.player.domain.repository.PlayerRepository
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    viewModel {
        PlayerViewModel(
            favouriteTracksInteractor = get(),
            playerInteractor = get()
        )
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

}
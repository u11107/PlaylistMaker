package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackApi
import com.example.playlistmaker.search.data.storage.SearchHistoryStorage
import com.example.playlistmaker.search.data.storage.SharedPrefsHistoryStorage
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    viewModel {
        SearchViewModel(
            get()
        )
    }

    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), searchHistoryStorage = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(api = get(), context = get())
    }

    single<SearchHistoryStorage> {
        SharedPrefsHistoryStorage(sharedPrefs = get())
    }

    single<TrackApi> {
        Retrofit.Builder().baseUrl("http://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }
}
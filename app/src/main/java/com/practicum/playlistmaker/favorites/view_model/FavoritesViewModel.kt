package com.practicum.playlistmaker.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val interactor: FavoritesInteractor) :
    ViewModel() {

    private val favoritesStateLiveData = MutableLiveData<FavoritesState>()
    fun observeFavoritesState(): LiveData<FavoritesState> = favoritesStateLiveData

    fun displayState() {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getFavorites().collect { tracks ->
                processResult(tracks)
            }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) favoritesStateLiveData.postValue(FavoritesState.EmptyFavorites)
        else favoritesStateLiveData.postValue(FavoritesState.DisplayFavorites(tracks))
    }
}
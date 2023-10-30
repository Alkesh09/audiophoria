package com.alkeshapp.audiophoria.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkeshapp.audiophoria.common.Resultat
import com.alkeshapp.audiophoria.domain.usecases.GetSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(private val getSongsUseCase: GetSongsUseCase) :
    ViewModel() {

    var songListState by mutableStateOf(SongListState())
        private set

    init {
        getSongs()
    }

    private fun getSongs() {
        getSongsUseCase().onEach { resultat ->
            songListState = when (resultat) {
                is Resultat.Success -> {
                    SongListState(songs = resultat.data ?: emptyList())
                }
                is Resultat.Error -> {
                    SongListState(error = resultat.message)
                }
                is Resultat.Loading -> {
                    SongListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
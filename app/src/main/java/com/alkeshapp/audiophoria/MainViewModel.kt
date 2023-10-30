package com.alkeshapp.audiophoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class MainViewModel @Inject constructor(private val songsRepository: SongRepository) : ViewModel() {
//    private val _songsList: MutableStateFlow<NetworkResponse<List<Song>>> =
//        MutableStateFlow(NetworkResponse.Loading)
//    val songsList: StateFlow<NetworkResponse<List<Song>>>
//        get() = _songsList
//
//
//    fun getSongs(){
//        viewModelScope.launch {
//            songsRepository.getSongsList().onEach {
//                _songsList.value = it
//            }.launchIn(viewModelScope)
//        }
//    }
//}
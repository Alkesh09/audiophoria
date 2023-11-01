package com.alkeshapp.audiophoria.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.alkeshapp.audiophoria.common.Resultat
import com.alkeshapp.audiophoria.domain.models.Song
import com.alkeshapp.audiophoria.domain.usecases.GetSongsUseCase
import com.alkeshapp.audiophoria.player.MusicPlayer
import com.alkeshapp.audiophoria.ui.util.PlayerEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val player: Player,
    private val getSongsUseCase: GetSongsUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    var isMiniPlayerVisible = mutableStateOf(false)

    private var _currentPlayingSong = MutableStateFlow(Song())
    val currentSongFlow = _currentPlayingSong.asStateFlow()

    private var _currentMediaPosition = MutableStateFlow<Float>(0f)
    val currentMediaPosition = _currentMediaPosition.asStateFlow()

    private var _isPausePlayClicked = MutableStateFlow(false)
    val isPausePlayClicked = _isPausePlayClicked.asStateFlow()

    private var _isPlayerBuffering = MutableStateFlow(false)
    val isPlayerBuffering = _isPlayerBuffering.asStateFlow()

    private var _currentSongDurationInMinutes = MutableStateFlow("0:00")
    val currentSongDurationInMinutes = _currentSongDurationInMinutes.asStateFlow()

    private var _currentSongProgressInMinutes = MutableStateFlow("0:00")
    val currentSongProgressInMinutes = _currentSongProgressInMinutes.asStateFlow()

    var songListState by mutableStateOf(SongListState())
        private set

    private val exoplayerInstance: MusicPlayer = MusicPlayer(
        exoplayer = player,
        currentSong = _currentPlayingSong,
        currentMediaPosition = _currentMediaPosition,
        currentMediaDurationInMinutes= _currentSongDurationInMinutes,
        currentMediaProgressInMinutes = _currentSongProgressInMinutes,
        isPausePlayClicked = _isPausePlayClicked,
        isPlayerBuffering = _isPlayerBuffering,
        viewModelScope = viewModelScope
    )

    init {
        player.addListener(exoplayerInstance)
        getSongs()
        exoplayerInstance.setupMediaNotification(context)
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

    fun onPlayerEvents(event: PlayerEvents) {
        when (event) {
            PlayerEvents.onPausePlay -> {
                exoplayerInstance.playPauseMusic()
            }
            is PlayerEvents.onPlayNewSong -> {
                _currentPlayingSong.value = event.song
                exoplayerInstance.performPlayNewSong(event.song)

                //everytime we need to refresh the playlist whenever user clicks a new song
                viewModelScope.launch {
                    exoplayerInstance.performPreparePlaylist(songListState.songs)
                }
            }
            PlayerEvents.onplayNextSong -> {
                if (player.hasNextMediaItem()) exoplayerInstance.performPlayNextSong()
                else {
                    exoplayerInstance.performPreparePlaylist(songListState.songs)
                }
            }
            PlayerEvents.onplayPreviousSong -> {
                if (player.hasPreviousMediaItem()) exoplayerInstance.performPlayPreviousSong()
            }
            is PlayerEvents.onChangeColorGradient -> {

            }
            is PlayerEvents.onseekMusicDone -> exoplayerInstance.onSeekMusicDone(event.value)

        }
    }

    override fun onCleared() {
        exoplayerInstance.performReleaseInstances() //just in case if Hilt is unable to for some unknown reasons
        Log.d("player", "onCleared called!!")
        super.onCleared()
    }

}
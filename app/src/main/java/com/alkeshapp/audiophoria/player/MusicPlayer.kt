package com.alkeshapp.audiophoria.player


import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alkeshapp.audiophoria.domain.models.Song
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MusicPlayer @Inject constructor(
    private val exoplayer: Player,
    private var currentSong: MutableStateFlow<Song>,
    private var currentMediaPosition: MutableStateFlow<Float>,
    private var currentMediaDurationInMinutes: MutableStateFlow<String>,
    private var currentMediaProgressInMinutes: MutableStateFlow<String>,
    private var isPausePlayClicked: MutableStateFlow<Boolean>,
    private var isPlayerBuffering: MutableStateFlow<Boolean>,
    private val viewModelScope: CoroutineScope
) : Player.Listener {

    val mapSongMediaItem = hashMapOf<String, Song>()
    var duration: Long = 0
    lateinit var controller: ListenableFuture<MediaController>

    fun playPauseMusic() {
        if (exoplayer.isPlaying) {
            exoplayer.pause()
        } else {
            exoplayer.play()
        }
    }

    fun performPlayNewSong(song: Song) {
        val metadata = getMetaDataFromSong(song)
        val mediaItem = MediaItem.Builder()
            .setUri(song.url)
            .setMediaMetadata(metadata)
            .build()

        exoplayer.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
        currentSong.value.isBackgroundColorEnabled = false
        currentSong.value = song
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        currentMediaPosition.value = 0f

        val song = mapSongMediaItem.get(mediaItem.toString())
        if (song != null) {
            currentSong.value.isBackgroundColorEnabled = false
            currentSong.value = song
            currentSong.value.isBackgroundColorEnabled = true
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        isPausePlayClicked.value = isPlaying
    }

    fun performPreparePlaylist(songList: List<Song>) {
        for (song in songList) {
            val metadata = getMetaDataFromSong(song)
            val mediaItem = MediaItem.Builder().apply {
                setUri(song.url)
                setMediaMetadata(metadata)
            }.build()
            exoplayer.addMediaItem(mediaItem)
            mapSongMediaItem.put(mediaItem.toString(), song)
        }
        exoplayer.prepare()
    }

    fun performPlayNextSong() {
        exoplayer.seekToNextMediaItem()
    }

    fun performPlayPreviousSong() {
        exoplayer.seekToPreviousMediaItem()
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_ENDED -> {
                if (exoplayer.hasNextMediaItem()) {
                    if(exoplayer.hasNextMediaItem()) performPlayNextSong()
                }
            }
            Player.STATE_BUFFERING -> {
                isPlayerBuffering.value = true
                currentMediaProgressInMinutes.value = "00:00"
                currentMediaDurationInMinutes.value = "00:00"
            }
            Player.STATE_IDLE -> {
            }
            Player.STATE_READY -> {
                isPlayerBuffering.value = false
            }
        }
    }

    fun onSeekMusicDone(value: Float) {
        val longValue = (value * duration).toLong()
        exoplayer.seekTo(longValue)
    }

    fun getMetaDataFromSong(song: Song): MediaMetadata {
        return MediaMetadata.Builder()
            .setTitle(song.name)
            .setAlbumTitle(song.name)
            .setDisplayTitle(song.name)
            .setArtist(song.artist)
            .setAlbumArtist(song.artist)
            .setArtworkUri(song.url.toUri())
            .build()
    }


    fun updatePlayerSeekProgress(pos: Long) {
        currentMediaProgressInMinutes.value = convertDurationLongToTime(pos)
        val progress = pos.toFloat() / duration.toFloat()
        if (!progress.isNaN()) currentMediaPosition.value = progress
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
    }

    fun performReleaseInstances() {
        exoplayer.release()
        MediaController.releaseFuture(controller)
    }

    fun convertDurationLongToTime(getDurationInMillis: Long): String {
        val convertMinutes = String.format(
            "%02d", TimeUnit.MILLISECONDS.toMinutes(getDurationInMillis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getDurationInMillis))
        ) //I needed to add this part.
        val convertSeconds = String.format(
            "%02d", TimeUnit.MILLISECONDS.toSeconds(getDurationInMillis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getDurationInMillis))
        )
        return "$convertMinutes:$convertSeconds"
    }

    fun setupMediaNotification(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicService::class.java))
        controller = MediaController.Builder(context, sessionToken).buildAsync()
        controller.addListener({
            val mediaController = controller.get()
            mediaController.addListener(object : Player.Listener {
                //to sync with inside app player media play/pause button
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    isPausePlayClicked.value = isPlaying
                    duration = mediaController.duration
                    if (duration == -9223372036854775807) duration =
                        0   //buffering is throwing back this number
                    currentMediaDurationInMinutes.value = convertDurationLongToTime(duration)
                    viewModelScope.launch {
                        while (isPausePlayClicked.value) {
                            updatePlayerSeekProgress(exoplayer.currentPosition)
                            delay(1000)
                        }
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    when (reason) {
                        Player.DISCONTINUITY_REASON_SEEK -> {
                            updatePlayerSeekProgress(newPosition.contentPositionMs)
                            exoplayer.seekTo(newPosition.contentPositionMs)
                        }
                        Player.DISCONTINUITY_REASON_AUTO_TRANSITION -> Unit
                        Player.DISCONTINUITY_REASON_INTERNAL -> Unit
                        Player.DISCONTINUITY_REASON_REMOVE -> Unit
                        Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT -> Unit
                        Player.DISCONTINUITY_REASON_SKIP -> Unit
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }
}
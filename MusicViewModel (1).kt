package com.neonplayer.music.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neonplayer.music.data.MusicRepository
import com.neonplayer.music.model.PlayerState
import com.neonplayer.music.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository(application)

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        scanLocalMusic()
    }

    fun scanLocalMusic() {
        viewModelScope.launch {
            val localTracks = repository.getLocalAudioTracks()
            _tracks.value = localTracks
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun playTrack(track: Track) {
        _playerState.value = _playerState.value.copy(
            currentTrack = track,
            isPlaying = true
        )
    }

    fun togglePlayPause() {
        _playerState.value = _playerState.value.copy(
            isPlaying = !_playerState.value.isPlaying
        )
    }
}
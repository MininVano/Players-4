package com.neonplayer.music.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: String,
    val albumArtUri: String = "",
    val filePath: String = ""
)

data class PlayerState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val currentTime: Long = 0,
    val duration: Long = 0
)
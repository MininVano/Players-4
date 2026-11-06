package com.neonplayer.music.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.neonplayer.music.MainActivity
import com.neonplayer.music.audio.EqualizerManager

class PlaybackService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var equalizerManager: EqualizerManager? = null

    override fun onCreate() {
        super.onCreate()

        // Настройка параметров воспроизведения звука
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true) // Обработка аудиофокуса (звонки, другие приложения)
            .setHandleAudioBecomingNoisy(true) // Пауза при отключении наушников/Bluetooth
            .build()

        // Инициализация аппаратного эквалайзера для AudioSessionId плеера
        player?.audioSessionId?.let { sessionId ->
            if (sessionId != C.AUDIO_SESSION_ID_UNSET) {
                equalizerManager = EqualizerManager(sessionId)
            }
        }

        // Клик по системному уведомлению открывает приложение
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, player!!)
            .setSessionActivity(pendingIntent)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        equalizerManager?.release()
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
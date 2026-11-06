package com.neonplayer.music.audio

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer

class EqualizerManager(private val audioSessionId: Int) {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null

    init {
        try {
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = true
            }
            bassBoost = BassBoost(0, audioSessionId).apply {
                enabled = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getNumberOfBands(): Short = equalizer?.numberOfBands ?: 0

    fun getBandFrequency(band: Short): Int = equalizer?.getCenterFreq(band)?.div(1000) ?: 0

    fun getBandLevelRange(): Pair<Short, Short> {
        val range = equalizer?.bandLevelRange ?: shortArrayOf(-1200, 1200)
        return Pair(range[0], range[1])
    }

    fun setBandLevel(band: Short, level: Short) {
        try {
            equalizer?.setBandLevel(band, level)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setBassBoostStrength(strength: Short) { // 0 to 1000
        try {
            bassBoost?.setStrength(strength)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun usePreset(presetIndex: Short) {
        try {
            equalizer?.usePreset(presetIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        equalizer?.release()
        bassBoost?.release()
        equalizer = null
        bassBoost = null
    }
}
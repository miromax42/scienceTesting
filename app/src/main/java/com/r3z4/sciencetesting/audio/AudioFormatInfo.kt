package com.r3z4.sciencetesting.audio

import android.media.AudioFormat.CHANNEL_IN_MONO
import android.media.AudioFormat.ENCODING_PCM_16BIT

class AudioFormatInfo {
    fun getSampleRateInHz(): Int {
        return 44100
    }

    fun getChannelConfig(): Int {
        return CHANNEL_IN_MONO
    }

    fun getAudioFormat(): Int {
        return ENCODING_PCM_16BIT
    }
}
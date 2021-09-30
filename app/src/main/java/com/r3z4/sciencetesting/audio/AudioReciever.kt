package com.r3z4.sciencetesting

import android.annotation.SuppressLint
import android.media.AudioRecord

import android.media.MediaRecorder
import java.lang.IllegalStateException
import android.media.AudioFormat;
import android.os.Handler;
import android.os.Process;
import com.r3z4.sciencetesting.audio.AudioFormatInfo


class AudioReciever(format: AudioFormatInfo) : Runnable {
    private var mIsRunning: Boolean
    private var handling: (ShortArray,Long) -> Unit = { shorts: ShortArray, l: Long -> }
    private val format: AudioFormatInfo
    private var mRecord: AudioRecord?
    private val BUFF_COUNT = 32
    fun addHandler(handler: (ShortArray,Long) -> Unit) {
        handling=handler
    }

    fun stop() {
        mIsRunning = false

    }

    @SuppressLint("MissingPermission")
    override fun run() {
        // приоритет для потока обработки аудио
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        mIsRunning = true
        val buffSize = AudioRecord.getMinBufferSize(
            format.getSampleRateInHz(),
            format.getChannelConfig(),
            format.getAudioFormat()
        )
        if (buffSize == AudioRecord.ERROR) {
            System.err.println("getMinBufferSize returned ERROR")
            return
        }
        if (buffSize == AudioRecord.ERROR_BAD_VALUE) {
            System.err.println("getMinBufferSize returned ERROR_BAD_VALUE")
            return
        }

        // здесь работаем с short, поэтому требуем 16-bit
        if (format.getAudioFormat() !== AudioFormat.ENCODING_PCM_16BIT) {
            System.err.println("unknown format")
            return
        }

        // циклический буфер буферов. Чтобы не затереть данные,
        // пока главный поток их обрабатывает
        val buffers = Array(BUFF_COUNT) {
            ShortArray(
                buffSize shr 1
            )
        }
        mRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            format.getSampleRateInHz(),
            format.getChannelConfig(), format.getAudioFormat(),
            buffSize * 10
        )
        if (mRecord!!.state != AudioRecord.STATE_INITIALIZED) {
            System.err.println("getState() != STATE_INITIALIZED")
            return
        }
        try {
            mRecord!!.startRecording()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return
        }
        var count = 0
        while (mIsRunning) {
            val samplesRead = mRecord!!.read(buffers[count], 0, buffers[count].count())
            if (samplesRead == AudioRecord.ERROR_INVALID_OPERATION) {
                System.err.println("read() returned ERROR_INVALID_OPERATION")
                return
            }
            if (samplesRead == AudioRecord.ERROR_BAD_VALUE) {
                System.err.println("read() returned ERROR_BAD_VALUE")
                return
            }

            // посылаем оповещение обработчикам
            sendMsg(buffers[count],System.currentTimeMillis())
            count = (count + 1) % BUFF_COUNT
        }
        try {
            try {
                mRecord!!.stop()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                return
            }
        } finally {
            // освобождаем ресурсы
            mRecord!!.release()
            mRecord = null
        }
    }

    private fun sendMsg(data: ShortArray,time:Long) {
        handling(data,time)
    }

    init {
        this.format = format
        mIsRunning = true
        mRecord = null
    }
}
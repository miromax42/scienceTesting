package com.r3z4.sciencetesting.calibration

import android.Manifest
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder.AudioSource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CalibrationFragment : Fragment() {

    companion object {
        fun newInstance() = CalibrationFragment()
    }

    private lateinit var viewModel: CalibrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(requireActivity(), permissions,0)
        }

        return inflater.inflate(com.r3z4.sciencetesting.R.layout.calibration_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalibrationViewModel::class.java)
        // TODO: Use the ViewModel
    }
    var recordingThread: Thread? = null
    var isRecording = false


    var audioSource = AudioSource.MIC
    var sampleRateInHz = 44100
    var channelConfig: Int = AudioFormat.CHANNEL_IN_MONO
    var audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT
    var bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)

    var Data = ByteArray(bufferSizeInBytes)

    @SuppressLint("MissingPermission")
    var audioRecorder: AudioRecord? = AudioRecord(
        audioSource,
        sampleRateInHz,
        channelConfig,
        audioFormat,
        bufferSizeInBytes)


    fun startRecording() {
        audioRecorder!!.startRecording()
        isRecording = true
        recordingThread = Thread {
            val filepath = requireContext().getExternalFilesDir(null)?.absolutePath
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream("$filepath/record.pcm")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            while (isRecording) {
                audioRecorder!!.read(Data, 0, Data.size)
                try {
                    os!!.write(Data, 0, bufferSizeInBytes)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    os!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        recordingThread!!.start()
    }

    fun stopRecording() {
        if (null != audioRecorder) {
            isRecording = false
            audioRecorder!!.stop()
            audioRecorder!!.release()
            audioRecorder = null
            recordingThread = null
        }
    }

}
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
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.r3z4.sciencetesting.AudioReciever
import com.r3z4.sciencetesting.R
import com.r3z4.sciencetesting.audio.AudioFormatInfo
import com.r3z4.sciencetesting.databinding.CalibrationFragmentBinding
import com.r3z4.sciencetesting.databinding.TestFragmentBinding
import com.r3z4.sciencetesting.test.TestViewModel
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CalibrationFragment : Fragment() {

    companion object {
        fun newInstance() = CalibrationFragment()
    }

    private lateinit var viewModel: CalibrationViewModel
    private lateinit var binding: CalibrationFragmentBinding
    private lateinit var audioFormatInfo: AudioFormatInfo
    private lateinit var audioReceiver: AudioReciever


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,R.layout.calibration_fragment,container,false)
        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this).get(CalibrationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel=viewModel

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(requireActivity(), permissions,0)
        }

        audioFormatInfo=AudioFormatInfo()
        audioReceiver=AudioReciever(audioFormatInfo)
        audioReceiver.addHandler { p1->viewModel.wav.value!!.add(p1) }

        var recordingThread:Thread

        binding.buttonStart.setOnClickListener {
            viewModel.wav.value= mutableListOf()
            viewModel.taps.value= mutableListOf()
            viewModel.startWav.value=System.currentTimeMillis()
            recordingThread=Thread{
                Log.i("thread","start thread")
                audioReceiver.run()
            }
            recordingThread.start()
        }

        binding.buttonStop.setOnClickListener {
            audioReceiver.stop()
        }

        binding.buttonShow.setOnClickListener {
            viewModel.show()
        }
        binding.buttonView.setOnClickListener {
            Log.i("audio","view tapped")
            viewModel.taps.value?.add(System.currentTimeMillis())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalibrationViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
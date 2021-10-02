package com.r3z4.sciencetesting.calibration

import android.Manifest
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.r3z4.sciencetesting.AudioReciever
import com.r3z4.sciencetesting.R
import com.r3z4.sciencetesting.audio.AudioFormatInfo
import com.r3z4.sciencetesting.databinding.CalibrationFragmentBinding


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
        audioReceiver.addEndHandler { viewModel.running.value=false }

        var recordingThread:Thread

        binding.buttonStart.setOnClickListener {
            viewModel.running.value=true
            viewModel.wav.value= mutableListOf()
            viewModel.taps.value= mutableListOf()
//            viewModel.startWav.value=System.currentTimeMillis()
            recordingThread=Thread{
                Log.i("thread","start thread")
                audioReceiver.run()
            }
            recordingThread.start()
        }

        binding.buttonStop.setOnClickListener {
            audioReceiver.stop()
            viewModel.startWav.value=audioReceiver.startTime
//            viewModel.running.value=false

        }
        viewModel.running.observe(viewLifecycleOwner){
            if (!it){
                viewModel.show()
            }
        }
        binding.buttonAddDelay.setOnClickListener {
            if (viewModel.delay.value!=0L) {
                viewModel.delayArray.value!!.add(viewModel.delay.value!!)
                viewModel.delayArray.value=viewModel.delayArray.value
                Log.d("audio",viewModel.delayArray.value.toString())
            }
        }

        binding.buttonReset.setOnClickListener {
            viewModel.delayArray.value= mutableListOf()
        }


        binding.buttonView.setOnClickListener {
            Log.i("audio","view tapped")
            viewModel.taps.value?.add(System.currentTimeMillis())
        }

        binding.buttonBackWithDelay.setOnClickListener {
            val bundle=bundleOf("delay" to viewModel.averageDelay.value)
            findNavController().navigate(R.id.testFragment,bundle)

    }



            return binding.root
        }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalibrationViewModel::class.java)

    }


}
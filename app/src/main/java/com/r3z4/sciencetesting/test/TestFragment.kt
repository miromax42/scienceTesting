package com.r3z4.sciencetesting.test

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.r3z4.sciencetesting.AudioReciever
import com.r3z4.sciencetesting.R
import com.r3z4.sciencetesting.audio.AudioFormatInfo
import com.r3z4.sciencetesting.databinding.TestFragmentBinding
import java.util.logging.Level.INFO

class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    private lateinit var binding: TestFragmentBinding
    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Main settings
        binding = DataBindingUtil.inflate(
            inflater, R.layout.test_fragment, container, false)
        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel=viewModel

        viewModel.isMeasuring.observe(viewLifecycleOwner, {
            if (it) binding.reationButton.setBackgroundColor(Color.GREEN) else binding.reationButton.setBackgroundColor(Color.WHITE)
        })


        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(activity!!, permissions,0)
        }

        val audioFormatInfo=AudioFormatInfo()
        val audioReceiver=AudioReciever(audioFormatInfo)
        audioReceiver.addHandler { p1,p2->wav.add(Pair(p1,p2)) }

        var recordingThread:Thread

        binding.buttonToggleCalibration.setOnClickListener {
            if (!viewModel.isCalibrating.value!!){
                recordingThread=Thread{
                    Log.i("thread","start thread")
                    audioReceiver.run()
                }
                recordingThread.start()
                viewModel.isCalibrating.value=!(viewModel.isCalibrating.value!!)
            }else{
                audioReceiver.stop()
                viewModel.isCalibrating.value=!(viewModel.isCalibrating.value!!)
                Log.i("audio",wav.toString())
            }
        }

        return binding.root
    }

    val wav= mutableListOf<Pair<ShortArray,Long>>()

    private fun Toaster(data:String) {
        Toast.makeText(context!!,data,Toast.LENGTH_SHORT).show()
    }



}
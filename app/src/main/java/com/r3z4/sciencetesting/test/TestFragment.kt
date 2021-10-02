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
import androidx.navigation.Navigation
import com.r3z4.sciencetesting.AudioReciever
import com.r3z4.sciencetesting.R
import com.r3z4.sciencetesting.audio.AudioFormatInfo
import com.r3z4.sciencetesting.databinding.TestFragmentBinding
import java.util.logging.Level.INFO
import kotlin.math.abs

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
        viewModel.delayValue.value =arguments?.getLong("delay")



        binding.buttonCalibrate.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.calibrationkFragment, null))



        return binding.root
    }

//    var wav= mutableListOf<Pair<ShortArray,Long>>()
//    var taps= mutableListOf<Long>()
//
//    fun printWav(wav:MutableList<Pair<ShortArray,Long>>):String {
//        val sum2=wav.map { Pair(abs(it.first.maxByOrNull { x->x }!!.toInt()),it.second) }
//        val sum3=sum2.filter { (it.first!!>100 )or (it.second!!.toInt()<(-100))}
//        val sum4=sum2.sortedBy { -it.first }
//        val sum= wav.map { Pair(it.first.sum() ,it.second)}
//        val sum5=wav.map{it.first.size}
//        return sum4.toString()
//    }
//
//
//    private fun Toaster(data:String) {
//        Toast.makeText(context!!,data,Toast.LENGTH_SHORT).show()
//    }



}
package com.r3z4.sciencetesting.test

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.r3z4.sciencetesting.R
import com.r3z4.sciencetesting.databinding.TestFragmentBinding

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



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }

}
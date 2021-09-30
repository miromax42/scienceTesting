package com.r3z4.sciencetesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.compose.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.r3z4.sciencetesting.test.TestFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

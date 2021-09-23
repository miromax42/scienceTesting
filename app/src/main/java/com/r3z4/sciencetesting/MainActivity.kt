package com.r3z4.sciencetesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.r3z4.sciencetesting.test.TestFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TestFragment.newInstance())
                .commitNow()
        }
    }
}

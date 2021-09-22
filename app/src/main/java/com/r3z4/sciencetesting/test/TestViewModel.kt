package com.r3z4.sciencetesting.test

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TestViewModel : ViewModel() {
    val lastTime= MutableLiveData<Int>(0)
    val averageTime= MutableLiveData<Int>(0)
    val allTime= MutableLiveData<Int>(0)

    val lastTimeS= Transformations.map(lastTime){it.toString()}
    val averageTimeS= Transformations.map(averageTime){it.toString()}
    val allTimeS= Transformations.map(allTime){it.toString()}

    val isPlaying=MutableLiveData(false)
    val playingText=Transformations.map(isPlaying){ if (it)  "Стоп" else "Старт"}


    fun userClicked() {
        isPlaying.value = !(isPlaying.value)!!

    }

    fun startClicked() {
        isPlaying.value = !(isPlaying.value)!!

    }
}
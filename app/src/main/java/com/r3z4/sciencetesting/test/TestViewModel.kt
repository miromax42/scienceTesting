package com.r3z4.sciencetesting.test

import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper.getMainLooper
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class TestViewModel : ViewModel() {
    val lastTime= MutableLiveData(0L)
    val averageTime= MutableLiveData(0L)
    val allTime= MutableLiveData(0f)

    val lastTimeS= Transformations.map(lastTime){(it).toString()}
    val averageTimeS= Transformations.map(averageTime){it.toString()}
    val allTimeS= Transformations.map(allTime){
        it.format(1)
    }

    val delayValue=MutableLiveData(0L)

    val isPlaying=MutableLiveData(false)
    val playingText=Transformations.map(isPlaying){ if (it)  "Стоп" else "Старт"}

    val isMeasuring=MutableLiveData(false)
//    val squareColor=Transformations.map(isMeasuring){ if (it)  Color.parseColor("#FF6200EE") else Color.parseColor("#000000")}

    val maxTime=10000L
    val interval=100L
    private val timer=object : CountDownTimer(maxTime, interval) {
        override fun onTick(millisUntilFinished: Long) {
            allTime.value= (maxTime-millisUntilFinished).toFloat()/1000
        }

        override fun onFinish() {
            reset()
        }
    }

    var timeStart:Long=0
    var timeEnd:Long=0
    var cnt=0


    fun userClicked() {
        if (isMeasuring.value!!){
            isMeasuring.value=false
            timeEnd=System.currentTimeMillis()
            lastTime.value=timeEnd-timeStart-(delayValue.value?:0L)
            cnt+=1
            averageTime.value=(averageTime.value!!*(cnt-1)/cnt)+(lastTime.value!!/cnt)
        }
        reset()

    }

    fun startClicked() {
        isPlaying.value = !(isPlaying.value)!!
        if (isPlaying.value!!){
            Handler(getMainLooper()).postDelayed({
                startMeasuring()
            }, (1000..5000).random().toLong())
            timer.start()
        }
        else{
            reset()
        }
    }

    fun startMeasuring(){
        if (isPlaying.value!!){
            isMeasuring.value=true
            timeStart=System.currentTimeMillis()
        }
    }

    fun reset(){
        allTime.value=0f
//        lastTime.value=0
        isPlaying.value=false
        isMeasuring.value=false
        timer.cancel()
    }


    //calibration
    val isCalibrating=MutableLiveData(false)
    val calibrationText=Transformations.map(isCalibrating){ if (it)  "Остановить" else "Откалибровать"}

    val phoneDelay=MutableLiveData(0L)

    fun toggleCalibration(){
        isCalibrating.value=!(isCalibrating.value)!!
        if (isCalibrating.value!!){
            startRecording()
        }else{
            stopRecording()
        }
    }
    fun calibrationSensorInput(){
        isCalibrating.value=!(isCalibrating.value)!!
    }

    fun startRecording(){

    }


    fun stopRecording(){

    }
    //new calibration

    private fun Float.format(digits: Int): String  = "%.${digits}f".format(this)
}



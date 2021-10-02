package com.r3z4.sciencetesting.calibration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.r3z4.sciencetesting.audio.AudioFormatInfo

class CalibrationViewModel : ViewModel() {
    val wav= MutableLiveData(mutableListOf<ShortArray>())
    val startWav=MutableLiveData(0L)
    val taps=MutableLiveData(mutableListOf<Long>())

    val delay=MutableLiveData(0L)
    val delayArray=MutableLiveData(mutableListOf<Long>())
    val averageDelay=Transformations.map(delayArray){
        Log.d("audioDelay","Transform called")
        if (it.count()>0) {
            it.average().toLong()
        }else{
            0L
        }
    }
    val running=MutableLiveData(false)
    val startable=Transformations.map(running){ !it }
    val stopable=Transformations.map(running){it}
    val resetable=Transformations.map(delayArray){ it.isNotEmpty()  }
    val addable=Transformations.map(delay){it!=0L}

    val showVisible=MutableLiveData(false)
    val startVisible=Transformations.map(showVisible){!it}



    fun show(){
        if ((wav.value?.isNotEmpty() == true) and (taps.value?.isNotEmpty() == true)) {
            Log.i("audioWav", printWav())
            Log.i("audioTaps", taps.value.toString())
        }
    }
    fun printWav():String{
        val audioFormatInfo=AudioFormatInfo()
        val indexToDelay = { i:Int->
            (((1000f/(audioFormatInfo.getSampleRateInHz().toFloat()))*i.toFloat())).toInt()
        }
        Log.i("audio",indexToDelay(8000).toString())
        val micValues= mutableListOf<Short>()
        wav.value?.forEach {
            it.forEach { s->
                micValues.add(s)
            }
        }

        var mappedMicValues=micValues.mapIndexed() { i,elem->
            Pair(elem, startWav.value?.plus(indexToDelay(i)))
        }

        mappedMicValues=mappedMicValues.filter {
            val diff=((taps.value?.get(0) ?: 0L) - it.second!!)
            ((diff<500L) and (diff>20L))
        }
        mappedMicValues=mappedMicValues.sortedBy { -it.first }


//        val delayArray= mutableListOf<Long>()
//        taps.value?.forEach {tapTime->
//            var micValuesForTap=mappedMicValues.filter { micPair->
//                val diff=tapTime-micPair.second!!
//                ((diff<300L) and (diff>20L))
//            }
//            Log.i("audioTapArray",micValuesForTap.toString())
//            if (micValues.count()>0){
//                val maxTapPair=micValuesForTap.first()
//                val diff=tapTime- maxTapPair.second!!
//                delayArray.add(diff)
//            }
//        }
//        Log.i("audioDelay",delayArray.toString())

//        val sortedDelayArray=delayArray.sortedBy { it }
//        delay.value= delayArray.average().toLong()
        delay.value= taps.value!![0]- mappedMicValues[0].second!!
//        delay.value=sortedDelayArray[sortedDelayArray.count()/2]


        return mappedMicValues.toString()
    }
}


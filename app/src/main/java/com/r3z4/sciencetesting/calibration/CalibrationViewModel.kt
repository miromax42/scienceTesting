package com.r3z4.sciencetesting.calibration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.r3z4.sciencetesting.audio.AudioFormatInfo

class CalibrationViewModel : ViewModel() {
    val wav= MutableLiveData(mutableListOf<ShortArray>())
    val startWav=MutableLiveData(0L)
    val taps=MutableLiveData(mutableListOf<Long>())

    val delay=MutableLiveData(0L)

    fun show(){
        Log.i("audioWav",printWav())
        Log.i("audioTaps",taps.value.toString())
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

        mappedMicValues=mappedMicValues.sortedBy { -it.first }

//        val delayArray= mutableListOf<Long>()

//        taps.value?.forEach {
//            var flag=true
//            for (micValue in mappedMicValues) {
//                val diff = micValue.second!! - it
//                if (flag and((diff < 300L) and (diff > 20L))) {
//                    delayArray.add(diff)
//                    flag=false
//                }
//
//            }
//
//        }
//        taps.value?.forEach {tapTime->
//            val micValuesForTap= mutableListOf<Pair<Short,Long>>()
//            mappedMicValues.forEach {
//                val diff=it.second!!-tapTime
//                if ((diff<300L) and (diff>20L)){
//                    micValuesForTap.add(it as Pair<Short, Long>)
//                }
//            }
//            Log.i("audioFiltered",micValuesForTap.toString())
//            val sortedMicValuesForTap=micValuesForTap.sortedBy { -it.first }
//            Log.i("audioSorted",sortedMicValuesForTap.toString())
//            if (micValuesForTap.count()>0){
//                val finalDiff=tapTime.minus(micValuesForTap[0].second)
//                delayArray.add(finalDiff)
//            }
//            Log.i("audioDelay",delayArray.toString())
//
//        }

//        Log.i("audioDelay",delayArray.toString())
//        delay.value= delayArray.average().toLong()
        delay.value= taps.value!![0]- mappedMicValues[0].second!!


        return mappedMicValues.toString()
    }
}
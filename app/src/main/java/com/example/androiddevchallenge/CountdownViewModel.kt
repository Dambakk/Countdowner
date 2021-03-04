package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CountdownViewModel : ViewModel() {

    var timeLeft: TimeLeftModel? by mutableStateOf(null)
    private var timer: CountDownTimer? = null

    fun startCountdown(timeInSeconds: Long) {
        timer?.cancel()
        timer = createNewTimer(timeInSeconds).also {
            it.start()
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timeLeft = null
    }

    private fun createNewTimer(timeInSeconds: Long): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = TimeLeftModel(millisUntilFinished / 1000)
            }
            override fun onFinish() {
                timeLeft = null
            }
        }
    }
}
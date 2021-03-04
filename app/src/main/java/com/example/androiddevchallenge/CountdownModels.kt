package com.example.androiddevchallenge

data class TimeLeftModel(
    val totalSeconds: Long
) {
    val minutes get() = totalSeconds / 60
    val tenSeconds get() = totalSeconds / 10
    val secondsInTen get() = totalSeconds % 10
    val secondsInMinute get() = totalSeconds % 60

    override fun toString(): String {
        return "$minutes : ${if (secondsInMinute < 10) "0$secondsInMinute" else secondsInMinute}"
    }
}
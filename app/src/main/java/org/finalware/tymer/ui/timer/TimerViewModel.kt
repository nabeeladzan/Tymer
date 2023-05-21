package org.finalware.tymer.ui.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.finalware.tymer.R
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Time

class TimerViewModel: ViewModel() {
    //from v1
    private var lastToastTime: Long = 0
    private var isCounting = false
    private var duration = Duration(-1, -1, -1)
    private var lastDuration = Duration(-1, -1, -1)
    private var timer: CountDownTimer? = null
    private val notValidDuration = Duration(-1, -1, -1)

    //from v2
    private val time = MutableLiveData<Time>()
    val beepSoundResId: LiveData<Int> = MutableLiveData()


    fun getCurrentTime(): MutableLiveData<Time> {
        return time
    }

    fun isCounting(): Boolean {
        return isCounting
    }

    fun stopTimer() {
        timer?.cancel()
        isCounting = false
    }

    fun playBeep() {
        (beepSoundResId as MutableLiveData).value = R.raw.beep
    }

    fun resumeTimer() {
        if (!isCounting) {
            isCounting = true
            timer = object : CountDownTimer(
                lastDuration.hours * 3600000L + lastDuration.minutes * 60000L + (lastDuration.seconds + 1) * 1000L,
                1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / 3600000
                    val minutes = (millisUntilFinished % 3600000) / 60000
                    val seconds = ((millisUntilFinished % 3600000) % 60000) / 1000
                    //format so always 2 digits
                    val hoursString = String.format("%02d", hours)
                    val minutesString = String.format("%02d", minutes)
                    val secondsString = String.format("%02d", seconds)
                    time.value = Time("$hoursString:$minutesString:$secondsString")

                    // set lastDuration
                    lastDuration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())
                }

                override fun onFinish() {
                    time.value = Time("00:00:00")
                    stopTimer()
                }
            }.start()
        }
    }

    fun startTimer(h: String, m: String, s: String) {
        var hours = h
        var minutes = m
        var seconds = s

        var toastIsShown = false

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > 1000) {
            toastIsShown = true
        }

        // check inputs if all inputs are empty and send toast
        if (hours.isEmpty() && minutes.isEmpty() && seconds.isEmpty()) {
            if (!toastIsShown) {
                //Toast.makeText(this, R.string.missing_input, Toast.LENGTH_LONG).show()
                lastToastTime = currentTime
            }
            duration = notValidDuration
            return
        }

        //set empty inputs to 0
        if (hours.isEmpty()) {
            hours = "0"
        }
        if (minutes.isEmpty()) {
            minutes = "0"
        }
        if (seconds.isEmpty()) {
            seconds = "0"
        }

        // check if negative
        if (hours.toInt() < 0 || minutes.toInt() < 0 || seconds.toInt() < 0) {
            if (!toastIsShown) {
                //Toast.makeText(this, R.string.input_negative, Toast.LENGTH_LONG).show()
                lastToastTime = currentTime
            }
            duration = notValidDuration
            return
        }

        // check if over 60
        if (seconds.toInt() > 60) {
            if (!toastIsShown) {
                //Toast.makeText(this, R.string.seconds_over_60, Toast.LENGTH_LONG).show()
                lastToastTime = currentTime
            }
            duration = notValidDuration
            return
        }

        // check if over 60
        if (minutes.toInt() > 60) {
            if (!toastIsShown) {
                //Toast.makeText(this, R.string.minutes_over_60, Toast.LENGTH_LONG).show()
                lastToastTime = currentTime
            }
            duration = notValidDuration
            return
        }

        // check if over 24
        if (hours.toInt() > 24) {
            if (!toastIsShown) {
                //Toast.makeText(this, R.string.hours_over_24, Toast.LENGTH_LONG).show()
                lastToastTime = currentTime
            }
            duration = notValidDuration
            return
        }

        // update duration
        duration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())

        if (duration.hours == -1 && duration.minutes == -1 && duration.seconds == -1) {
            Log.d("startTimer", "duration is -1 so input is invalid")
            return
        }

        isCounting = true

        // set timer
        timer = object : CountDownTimer(
            duration.hours * 3600000L + duration.minutes * 60000L + (duration.seconds + 1) * 1000L,
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / 3600000
                val minutes = (millisUntilFinished % 3600000) / 60000
                val seconds = ((millisUntilFinished % 3600000) % 60000) / 1000
                //format so always 2 digits
                val hoursString = String.format("%02d", hours)
                val minutesString = String.format("%02d", minutes)
                val secondsString = String.format("%02d", seconds)
                time.value = Time("$hoursString:$minutesString:$secondsString")

                // set lastDuration
                lastDuration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())
            }

            override fun onFinish() {
                time.value = Time("00:00:00")
                stopTimer()
            }
        }.start()
    }
}
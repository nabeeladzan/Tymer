package org.finalware.tymer

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import org.finalware.tymer.databinding.ActivityMainBinding
import androidx.core.content.ContextCompat;

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isCounting = false
    private var duration = Duration(-1, -1, -1)
    private var lastDuration = Duration(-1, -1, -1)
    private var timer: CountDownTimer? = null

    private val notValidDuration = Duration(-1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //full screen activity
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMain.setOnClickListener { startTimer() }
        binding.buttonReset.setOnClickListener { resetTimer() }
        binding.buttonTheme.setOnClickListener { toggleTheme() }

        // hide textViewCount
        binding.textViewCount.visibility = android.view.View.INVISIBLE
        binding.textViewCount.alpha = 0f
    }

    private fun toggleTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.buttonTheme.setTextColor(Color.BLACK)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.buttonTheme.setTextColor(Color.WHITE)
        }
    }

    // getTime
    private fun updateTime() {
        var hours = binding.inputHour.text.toString()
        var minutes = binding.inputMinute.text.toString()
        var seconds = binding.inputSecond.text.toString()

        // check inputs if all inputs are empty and send toast
        if (hours.isEmpty() && minutes.isEmpty() && seconds.isEmpty()) {
            Toast.makeText(this, R.string.missing_input, Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, R.string.input_negative, Toast.LENGTH_LONG).show()
            duration = notValidDuration
            return
        }

        // check if over 60
        if (seconds.toInt() > 60) {
            Toast.makeText(this, R.string.seconds_over_60, Toast.LENGTH_LONG).show()
            duration = notValidDuration
            return
        }

        // check if over 60
        if (minutes.toInt() > 60) {
            Toast.makeText(this, R.string.minutes_over_60, Toast.LENGTH_LONG).show()
            duration = notValidDuration
            return
        }

        // check if over 24
        if (hours.toInt() > 24) {
            Toast.makeText(this, R.string.hours_over_24, Toast.LENGTH_LONG).show()
            duration = notValidDuration
            return
        }

        // update duration
        duration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())
    }

    private fun startTimer() {
        if (isCounting) {
            Log.d("startTimer", "isCounting is true so return")
            return
        }

        updateTime()
        if (duration.hours == -1 && duration.minutes == -1 && duration.seconds == -1) {
            Log.d("startTimer", "duration is -1 so input is invalid")
            return
        }

        //disable input
        binding.inputHour.isEnabled = false
        binding.inputMinute.isEnabled = false
        binding.inputSecond.isEnabled = false

        // animated fade away of input
        binding.inputHour.animate().alpha(0f).duration = 150
        binding.inputMinute.animate().alpha(0f).duration = 150
        binding.inputSecond.animate().alpha(0f).duration = 150
        binding.textViewHour.animate().alpha(0f).duration = 150
        binding.textViewMinutes.animate().alpha(0f).duration = 150
        binding.textViewSeconds.animate().alpha(0f).duration = 150

        // animate fade in of textViewCount
        binding.textViewCount.visibility = android.view.View.VISIBLE
        binding.textViewCount.animate().alpha(1f).duration = 150

        // start timer
        isCounting = true
        binding.buttonMain.text = getString(R.string.stop)
        binding.buttonMain.setOnClickListener { stopTimer() }
        binding.buttonMain.setTextColor(ContextCompat.getColor(this, R.color.red))

        // set reset button to pause button
        binding.buttonReset.text = getString(R.string.pause)
        binding.buttonReset.setOnClickListener { pauseTimer() }

        //hide theme button
        binding.buttonTheme.visibility = android.view.View.INVISIBLE


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
                binding.textViewCount.text = "$hoursString:$minutesString:$secondsString"

                // set lastDuration
                lastDuration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())
            }

            override fun onFinish() {
                binding.textViewCount.text = "00:00:00"
                stopTimer()
            }
        }.start()
    }

    private fun stopTimer() {
        if (!isCounting) {
            Log.d("stopTimer", "isCounting is false so return")
            return
        }

        //enable input
        binding.inputHour.isEnabled = true
        binding.inputMinute.isEnabled = true
        binding.inputSecond.isEnabled = true

        // animated fade in of input
        binding.inputHour.animate().alpha(1f).duration = 150
        binding.inputMinute.animate().alpha(1f).duration = 150
        binding.inputSecond.animate().alpha(1f).duration = 150
        binding.textViewHour.animate().alpha(1f).duration = 150
        binding.textViewMinutes.animate().alpha(1f).duration = 150
        binding.textViewSeconds.animate().alpha(1f).duration = 150

        // set reset button to reset button
        binding.buttonReset.text = getString(R.string.reset)
        binding.buttonReset.setOnClickListener { resetTimer() }

        // play beep sound
        val mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer.start()

        // animate fade out of textViewCount
        binding.textViewCount.animate().alpha(0f).duration = 150
        binding.textViewCount.postDelayed({
            binding.textViewCount.visibility = android.view.View.INVISIBLE
        }, 150)

        //show theme button
        binding.buttonTheme.visibility = android.view.View.VISIBLE

        // stop timer
        isCounting = false
        binding.buttonMain.text = getString(R.string.start)
        binding.buttonMain.setOnClickListener { startTimer() }
        //set button text color to green
        binding.buttonMain.setTextColor(ContextCompat.getColor(this, R.color.teal_200))

        // cancel timer
        timer?.cancel()
    }

    private fun resetTimer() {
        if (isCounting) {
            stopTimer()
        }
        binding.inputHour.text?.clear()
        binding.inputMinute.text?.clear()
        binding.inputSecond.text?.clear()
    }

    private fun pauseTimer() {
        if (isCounting) {
            timer?.cancel()
            isCounting = false
            binding.buttonReset.text = getString(R.string.resume)
            binding.buttonReset.setOnClickListener { resumeTimer() }
        }
    }

    private fun resumeTimer() {
        if (!isCounting) {
            isCounting = true
            binding.buttonReset.text = getString(R.string.pause)
            binding.buttonReset.setOnClickListener { pauseTimer() }
            //set timer
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
                    binding.textViewCount.text = "$hoursString:$minutesString:$secondsString"

                    // set lastDuration
                    lastDuration = Duration(hours.toInt(), minutes.toInt(), seconds.toInt())
                }

                override fun onFinish() {
                    binding.textViewCount.text = "00:00:00"
                    stopTimer()
                }
            }.start()
        }
    }
}
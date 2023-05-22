package org.finalware.tymer.ui.timer

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.finalware.tymer.R
import org.finalware.tymer.databinding.FragmentTimerBinding
import org.finalware.tymer.datastore.SettingsDataStore
import org.finalware.tymer.datastore.dataStore


class FragmentTimer: Fragment(), toStop {
    private lateinit var binding: FragmentTimerBinding

    private val viewModel: TimerViewModel by lazy {
        ViewModelProvider(this).get(TimerViewModel::class.java)
    }

    private val args: FragmentTimerArgs by lazy {
        FragmentTimerArgs.fromBundle(requireArguments())
    }

    private val themeDataStore: SettingsDataStore by lazy {
        SettingsDataStore(requireContext().dataStore)
    }

    private var isDarkMode: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        viewModel.callback = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonTheme.setOnClickListener {
            toggleTheme()
        }
        binding.buttonReset.setOnClickListener {
            resetTimer()
        }
        binding.buttonMain.setOnClickListener {
            startTimer()
        }
        binding.buttonPreset.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTimer_to_fragmentPreset)
        }

        // hide textViewCount
        binding.textViewCount.visibility = android.view.View.INVISIBLE
        binding.textViewCount.alpha = 0f

        viewModel.getCurrentTime().observe(requireActivity()) {
            setTimeText(it.text)
        }

        viewModel.beepSoundResId.observe(viewLifecycleOwner) { resId ->
            resId?.let {
                val mediaPlayer = MediaPlayer.create(requireContext(), it)
                mediaPlayer.start()
            }
        }

            try {
                binding.inputHour.setText(args.hours.toString())
                binding.inputMinute.setText(args.minutes.toString())
                binding.inputSecond.setText(args.seconds.toString())
                startTimer()
            } catch (e: Exception) {}

        themeDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isDarkMode = it
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.buttonTheme.setTextColor(Color.WHITE)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.buttonTheme.setTextColor(Color.BLACK)
            }
        }
    }

    private fun setTimeText(time: String) {
        binding.textViewCount.text = time
    }

    private fun toggleTheme() {
        lifecycleScope.launch {
            themeDataStore.saveMode(!isDarkMode, requireContext())
        }
    }

    private fun resetTimer() {
        if (viewModel.isCounting()) {
            stopTimer()
        }
        binding.inputHour.text?.clear()
        binding.inputMinute.text?.clear()
        binding.inputSecond.text?.clear()
    }

    private fun pauseTimer() {
        if (viewModel.isCounting()) {
            viewModel.pauseTimer()
            binding.buttonReset.text = getString(R.string.resume)
            binding.buttonReset.setOnClickListener { resumeTimer() }
        }
    }

    private fun resumeTimer() {
        if (!viewModel.isCounting()) {
            viewModel.resumeTimer()
            binding.buttonReset.text = getString(R.string.pause)
            binding.buttonReset.setOnClickListener { pauseTimer() }
        }
    }

    private fun startTimer() {
        if (viewModel.isCounting()) {
            Log.d("startTimer", "isCounting is true so return")
            return
        }

        if (!validateInput()) {
            return
        }

        viewModel.startTimer(
            binding.inputHour.text.toString(),
            binding.inputMinute.text.toString(),
            binding.inputSecond.text.toString()
        )

        if (viewModel.isCounting()) {
            toStart()
        }
    }

    private fun stopTimer() {
        if (!viewModel.isCounting()) {
            Log.d("stopTimer", "isCounting is false so return")
            return
        }

        // cancel timer
        viewModel.stopTimer()
    }

    fun toStart() {


        //disable input
        binding.inputHour.isEnabled = false
        binding.inputMinute.isEnabled = false
        binding.inputSecond.isEnabled = false
        binding.buttonPreset.isEnabled = false

        // animated fade away of input
        binding.inputHour.animate().alpha(0f).duration = 150
        binding.inputMinute.animate().alpha(0f).duration = 150
        binding.inputSecond.animate().alpha(0f).duration = 150
        binding.buttonPreset.animate().alpha(0f).duration = 150
        binding.textViewHour.animate().alpha(0f).duration = 150
        binding.textViewMinutes.animate().alpha(0f).duration = 150
        binding.textViewSeconds.animate().alpha(0f).duration = 150

        // animate fade in of textViewCount
        binding.textViewCount.visibility = android.view.View.VISIBLE
        binding.textViewCount.animate().alpha(1f).duration = 150

        // start timer
        binding.buttonMain.text = getString(R.string.stop)
        binding.buttonMain.setOnClickListener { stopTimer() }
        binding.buttonMain.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

        // set reset button to pause button
        binding.buttonReset.text = getString(R.string.pause)
        binding.buttonReset.setOnClickListener { pauseTimer() }

        //hide theme button
        binding.buttonTheme.visibility = android.view.View.INVISIBLE
    }

    private fun validateInput(): Boolean {
        //check if input all input fields are empty
        if (binding.inputHour.text.isNullOrEmpty() &&
            binding.inputMinute.text.isNullOrEmpty() &&
            binding.inputSecond.text.isNullOrEmpty()
        ) {
            Toast.makeText(requireContext(), "Please enter atleast 1 field", Toast.LENGTH_SHORT).show()
            return false
        }

        //check if input is valid
        if (!binding.inputHour.text.isNullOrEmpty() && binding.inputHour.text.toString().toInt() > 23) {
            Toast.makeText(requireContext(), "Please enter a valid hour", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!binding.inputMinute.text.isNullOrEmpty() && binding.inputMinute.text.toString().toInt() > 59) {
            Toast.makeText(requireContext(), "Please enter a valid minute", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!binding.inputSecond.text.isNullOrEmpty() && binding.inputSecond.text.toString().toInt() > 59) {
            Toast.makeText(requireContext(), "Please enter a valid second", Toast.LENGTH_SHORT).show()
            return false
        }

        //check if negative
        if (!binding.inputHour.text.isNullOrEmpty() && binding.inputHour.text.toString().toInt() < 0) {
            Toast.makeText(requireContext(), "Please enter a valid hour", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!binding.inputMinute.text.isNullOrEmpty() && binding.inputMinute.text.toString().toInt() < 0) {
            Toast.makeText(requireContext(), "Please enter a valid minute", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!binding.inputSecond.text.isNullOrEmpty() && binding.inputSecond.text.toString().toInt() < 0) {
            Toast.makeText(requireContext(), "Please enter a valid second", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun toStop() {
        //enable input
        binding.inputHour.isEnabled = true
        binding.inputMinute.isEnabled = true
        binding.inputSecond.isEnabled = true
        binding.buttonPreset.isEnabled = true

        // animated fade in of input
        binding.inputHour.animate().alpha(1f).duration = 150
        binding.inputMinute.animate().alpha(1f).duration = 150
        binding.inputSecond.animate().alpha(1f).duration = 150
        binding.buttonPreset.animate().alpha(1f).duration = 150
        binding.textViewHour.animate().alpha(1f).duration = 150
        binding.textViewMinutes.animate().alpha(1f).duration = 150
        binding.textViewSeconds.animate().alpha(1f).duration = 150

        // set reset button to reset button
        binding.buttonReset.text = getString(R.string.reset)
        binding.buttonReset.setOnClickListener { resetTimer() }

        // play beep sound
        viewModel.playBeep()

        // animate fade out of textViewCount
        binding.textViewCount.animate().alpha(0f).duration = 150
        binding.textViewCount.postDelayed({
            binding.textViewCount.visibility = android.view.View.INVISIBLE
        }, 150)

        //show theme button
        binding.buttonTheme.visibility = android.view.View.VISIBLE

        binding.buttonMain.text = getString(R.string.start)
        binding.buttonMain.setOnClickListener { startTimer() }
        //set button text color to green
        binding.buttonMain.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
    }
}
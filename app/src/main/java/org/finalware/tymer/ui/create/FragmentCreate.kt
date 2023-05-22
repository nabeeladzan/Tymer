package org.finalware.tymer.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.databinding.FragmentCreateBinding

class FragmentCreate: Fragment() {
    private lateinit var binding: FragmentCreateBinding

    private val viewModel: CreateViewModel by lazy {
        val db = PresetDB.getDatabase(requireContext())
        CreateViewModel(db)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMain.setOnClickListener {
            createPreset()
        }
    }

    private fun createPreset() {
        // name cannot be empty
        if (binding.inputName.text.toString().isEmpty()) {
            binding.inputName.error = "Name cannot be empty"
            return
        }

        val name = binding.inputName.text.toString()
        var hours: Int
        var minutes: Int
        var seconds: Int

        if (binding.inputHour.text.toString().isEmpty()) {
            hours = 0
        } else {
            hours = binding.inputHour.text.toString().toInt()
        }

        if (binding.inputMinute.text.toString().isEmpty()) {
            minutes = 0
        } else {
            minutes = binding.inputMinute.text.toString().toInt()
        }

        if (binding.inputSecond.text.toString().isEmpty()) {
            seconds = 0
        } else {
            seconds = binding.inputSecond.text.toString().toInt()
        }

        val complete = viewModel.createPreset(name, hours, minutes, seconds)

        if (complete) {
            binding.inputName.text.clear()
            binding.inputHour.text?.clear()
            binding.inputMinute.text?.clear()
            binding.inputSecond.text?.clear()
            Toast.makeText(context, "Preset created", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Preset creation failed", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }
}
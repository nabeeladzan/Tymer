package org.finalware.tymer.ui.preset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.finalware.tymer.R
import org.finalware.tymer.databinding.PresetItemBinding
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset

class PresetAdapter(private val data: List<Preset>): RecyclerView.Adapter<PresetAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: PresetItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(preset: Preset) = with(binding) {
            button.setOnClickListener {
                val passdata = FragmentPresetDirections.actionFragmentPresetToFragmentTimer(
                    preset.duration.hours,
                    preset.duration.minutes,
                    preset.duration.seconds
                )
                binding.root.findNavController().navigate(passdata)
            }
            namaTextView.text = preset.name
            durationTextView.text =
                "${preset.duration.hours}:${preset.duration.minutes}:${preset.duration.seconds}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PresetItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun durationToInt(duration: Duration): Int {
        return duration.hours * 3600 + duration.minutes * 60 + duration.seconds
    }
}
package org.finalware.tymer.ui.preset

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.finalware.tymer.databinding.PresetItemBinding
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset

class PresetAdapter: RecyclerView.Adapter<PresetAdapter.ViewHolder>() {
    private val userPresets = mutableListOf<Preset>()
    private val presets = mutableListOf<Preset>()
    var viewHolderListener: ViewHolderListener? = null
    class ViewHolder(
        private val binding: PresetItemBinding, private val listener: ViewHolderListener
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(preset: Preset, userPreset: Boolean) = with(binding) {
            // if userPreset is true, then show deleteButton
            if (!userPreset) {
                deleteButton.visibility = ViewGroup.GONE
            } else {
                deleteButton.visibility = ViewGroup.VISIBLE
                deleteButton.setOnClickListener {
                    val alert = AlertDialog.Builder(binding.root.context)

                    alert.setTitle("Delete Preset")
                    alert.setMessage("Are you sure?")

                    alert.setPositiveButton("Ok") { dialog, whichButton ->
                        listener.deletePreset(preset)
                    }

                    alert.setNegativeButton("Cancel") { dialog, whichButton ->
                        // Canceled.
                    }

                    alert.show()
                }
            }

            selectButton.setOnClickListener {
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
        return ViewHolder(binding, viewHolderListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < presets.size) {
            holder.bind(presets[position], false)
        } else {
            holder.bind(userPresets[position - presets.size], true)
        }
    }

    override fun getItemCount(): Int {
        // return size of presets and userPresets combined
        return presets.size + userPresets.size
    }

    fun updatePresets(newData: List<Preset>) {
        presets.clear()
        presets.addAll(newData)
        notifyDataSetChanged()
    }

    fun updateUserPresets(newData: List<Preset>) {
        userPresets.clear()
        userPresets.addAll(newData)
        notifyDataSetChanged()
    }

    interface ViewHolderListener {
        fun deletePreset(preset: Preset)
    }
}
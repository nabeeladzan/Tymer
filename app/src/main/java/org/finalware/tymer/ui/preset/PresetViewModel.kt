package org.finalware.tymer.ui.preset

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.database.PresetEntity
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset

class PresetViewModel(private val db: PresetDB): ViewModel() {
    fun getPresets(): List<Preset> {
        val presets = mutableListOf<Preset>()
        GlobalScope.launch {
            val presetEntities = db.presetDao().getAll()
            presetEntities.forEach {
                presets.add(Preset(it.name!!, Duration(it.hours!!, it.minutes!!, it.seconds!!)))
            }
        }
        return presets
    }
}
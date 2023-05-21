package org.finalware.tymer.ui.preset

import androidx.lifecycle.ViewModel
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset

class PresetViewModel: ViewModel() {
    fun getPresets(): List<Preset> {
        return listOf(
            Preset("1 Minute", Duration(0, 1, 0)),
            Preset("5 Minutes", Duration(0, 5, 0)),
            Preset("10 Minutes", Duration(0 , 10, 0)),
        )
    }
}
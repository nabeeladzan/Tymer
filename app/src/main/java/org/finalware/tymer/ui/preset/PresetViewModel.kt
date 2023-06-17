package org.finalware.tymer.ui.preset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset

class PresetViewModel(private val db: PresetDB): ViewModel() {
    private val data = MutableLiveData<List<Preset>>()
    fun getPresets() {
        val presets = mutableListOf<Preset>()
        viewModelScope.launch (Dispatchers.IO) {
            val presetEntities = db.presetDao().getAll()
            presetEntities.forEach {
                presets.add(Preset(it.name!!, Duration(it.hours!!, it.minutes!!, it.seconds!!)))
            }
            data.postValue(presets)
        }
    }

    fun getData(): LiveData<List<Preset>> = data
}
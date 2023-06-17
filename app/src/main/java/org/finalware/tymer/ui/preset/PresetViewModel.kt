package org.finalware.tymer.ui.preset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.model.Duration
import org.finalware.tymer.model.Preset
import org.finalware.tymer.network.Api
import org.finalware.tymer.network.ApiStatus

class PresetViewModel(private val db: PresetDB): ViewModel() {
    private val userPresets = MutableLiveData<List<Preset>>()
    private val presets = MutableLiveData<List<Preset>>()
    private val status = MutableLiveData<ApiStatus>()
    fun getPresets() {
        val data = mutableListOf<Preset>()
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)

            try {
                presets.postValue(Api.service.getPresetsBin().record)
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("PresetViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }

            db.presetDao().getAll().forEach {
                data.add(Preset(it.name!!, Duration(it.hours!!, it.minutes!!, it.seconds!!)))
            }

            userPresets.postValue(data)
        }
    }

    fun getUserPresets(): LiveData<List<Preset>> = userPresets

    fun getServerPresets(): LiveData<List<Preset>> = presets

    fun getStatus(): LiveData<ApiStatus> = status

    fun deletePreset(preset: Preset): Boolean {
        return try {
            viewModelScope.launch (Dispatchers.IO) {
                db.presetDao().deletePreset(preset.name, preset.duration.hours, preset.duration.minutes, preset.duration.seconds)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
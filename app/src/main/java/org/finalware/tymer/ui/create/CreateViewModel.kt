package org.finalware.tymer.ui.create

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.database.PresetEntity

class CreateViewModel(private val db: PresetDB): ViewModel() {
    fun createPreset(name:String, hours:Int, minutes:Int, seconds:Int): Boolean {
        val preset = PresetEntity(0, name, hours, minutes, seconds)

        return try {
            GlobalScope.launch(Dispatchers.IO) {
                db.presetDao().insert(preset)
            }
            true
        } catch (e: Exception) {
            Log.e("CreateViewModel", "Error creating preset", e)
            false
        }

    }
}
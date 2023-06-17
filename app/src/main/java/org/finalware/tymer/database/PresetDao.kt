package org.finalware.tymer.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.finalware.tymer.model.Preset

@Dao
interface PresetDao {
    @Query("SELECT * FROM preset_table")
    fun getAll(): List<PresetEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(preset: PresetEntity)

    // delete with name and duration
    @Query("DELETE FROM preset_table WHERE name = :name AND hours = :hours AND minutes = :minutes AND seconds = :seconds")
    fun deletePreset(name: String, hours: Int, minutes: Int, seconds: Int)
}
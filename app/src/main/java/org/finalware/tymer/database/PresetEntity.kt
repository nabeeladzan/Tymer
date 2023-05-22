package org.finalware.tymer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preset_table")
data class PresetEntity(
    // auto generate primary key no need to pass it in
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "hours") val hours: Int?,
    @ColumnInfo(name = "minutes") val minutes: Int?,
    @ColumnInfo(name = "seconds") val seconds: Int?
)
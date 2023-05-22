package org.finalware.tymer.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PresetEntity::class], version = 1)
abstract class PresetDB : RoomDatabase() {
    abstract fun presetDao(): PresetDao

    companion object {
        @Volatile
        private var INSTANCE: PresetDB? = null

        fun getDatabase(context : Context): PresetDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    PresetDB::class.java,
                    "preset_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
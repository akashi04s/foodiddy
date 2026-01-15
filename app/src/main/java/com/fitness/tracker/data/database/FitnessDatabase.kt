package com.fitness.tracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitness.tracker.data.converter.Converters
import com.fitness.tracker.data.dao.CalorieEntryDao
import com.fitness.tracker.data.dao.WaterEntryDao
import com.fitness.tracker.data.dao.WeightEntryDao
import com.fitness.tracker.data.entity.CalorieEntry
import com.fitness.tracker.data.entity.WaterEntry
import com.fitness.tracker.data.entity.WeightEntry

@Database(
    entities = [WaterEntry::class, CalorieEntry::class, WeightEntry::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun waterEntryDao(): WaterEntryDao
    abstract fun calorieEntryDao(): CalorieEntryDao
    abstract fun weightEntryDao(): WeightEntryDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "fitness_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
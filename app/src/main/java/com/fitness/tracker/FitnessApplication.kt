package com.fitness.tracker

import android.app.Application
import com.fitness.tracker.data.database.FitnessDatabase
import com.fitness.tracker.data.preferences.UserPreferences
import com.fitness.tracker.data.repository.FitnessRepository

class FitnessApplication : Application() {
    private val database by lazy { FitnessDatabase.getDatabase(this) }
    private val userPreferences by lazy { UserPreferences(this) }
    
    val repository by lazy {
        FitnessRepository(
            waterEntryDao = database.waterEntryDao(),
            calorieEntryDao = database.calorieEntryDao(),
            weightEntryDao = database.weightEntryDao(),
            userPreferences = userPreferences
        )
    }
}
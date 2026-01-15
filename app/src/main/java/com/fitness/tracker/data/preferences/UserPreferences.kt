package com.fitness.tracker.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        private val WATER_TARGET_ML = intPreferencesKey("water_target_ml")
        private val CALORIE_TARGET = intPreferencesKey("calorie_target")
        private val WEIGHT_UNIT = stringPreferencesKey("weight_unit") // "kg" or "lbs"
        private val HEIGHT_CM = intPreferencesKey("height_cm")
        private val GENDER = stringPreferencesKey("gender") // "male" or "female"
        private val TARGET_WEIGHT_KG = floatPreferencesKey("target_weight_kg")
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val USER_NAME = stringPreferencesKey("user_name")
    }

    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }

    val waterTargetMl: Flow<Int> = dataStore.data.map { preferences ->
        preferences[WATER_TARGET_ML] ?: 2000
    }

    val calorieTarget: Flow<Int> = dataStore.data.map { preferences ->
        preferences[CALORIE_TARGET] ?: 2000
    }

    val weightUnit: Flow<String> = dataStore.data.map { preferences ->
        preferences[WEIGHT_UNIT] ?: "kg"
    }

    val heightCm: Flow<Int> = dataStore.data.map { preferences ->
        preferences[HEIGHT_CM] ?: 170
    }

    val gender: Flow<String> = dataStore.data.map { preferences ->
        preferences[GENDER] ?: "male"
    }

    val targetWeightKg: Flow<Float> = dataStore.data.map { preferences ->
        preferences[TARGET_WEIGHT_KG] ?: 70f
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: true // Default to dark mode
    }

    val userName: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    suspend fun setFirstLaunchCompleted() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    suspend fun setWaterTarget(targetMl: Int) {
        dataStore.edit { preferences ->
            preferences[WATER_TARGET_ML] = targetMl
        }
    }

    suspend fun setCalorieTarget(target: Int) {
        dataStore.edit { preferences ->
            preferences[CALORIE_TARGET] = target
        }
    }

    suspend fun setWeightUnit(unit: String) {
        dataStore.edit { preferences ->
            preferences[WEIGHT_UNIT] = unit
        }
    }

    suspend fun setHeight(heightCm: Int) {
        dataStore.edit { preferences ->
            preferences[HEIGHT_CM] = heightCm
        }
    }

    suspend fun setGender(gender: String) {
        dataStore.edit { preferences ->
            preferences[GENDER] = gender
        }
    }

    suspend fun setTargetWeight(weightKg: Float) {
        dataStore.edit { preferences ->
            preferences[TARGET_WEIGHT_KG] = weightKg
        }
    }

    suspend fun setDarkMode(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
        }
    }

    suspend fun setUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
}
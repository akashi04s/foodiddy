package com.fitness.tracker.data.repository

import com.fitness.tracker.data.dao.CalorieEntryDao
import com.fitness.tracker.data.dao.WaterEntryDao
import com.fitness.tracker.data.dao.WeightEntryDao
import com.fitness.tracker.data.entity.CalorieEntry
import com.fitness.tracker.data.entity.WaterEntry
import com.fitness.tracker.data.entity.WeightEntry
import com.fitness.tracker.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

class FitnessRepository(
    private val waterEntryDao: WaterEntryDao,
    private val calorieEntryDao: CalorieEntryDao,
    private val weightEntryDao: WeightEntryDao,
    private val userPreferences: UserPreferences
) {
    fun getWaterEntriesForDate(date: String): Flow<List<WaterEntry>> =
        waterEntryDao.getEntriesByDate(date)

    fun getTotalWaterForDate(date: String): Flow<Int?> =
        waterEntryDao.getTotalForDate(date)

    suspend fun addWaterEntry(amountMl: Int) {
        val now = LocalDateTime.now()
        val date = now.toLocalDate().toString()
        waterEntryDao.insert(
            WaterEntry(
                amountMl = amountMl,
                timestamp = now,
                date = date
            )
        )
    }

    fun getCalorieEntriesForDate(date: LocalDate): Flow<List<CalorieEntry>> =
        calorieEntryDao.getEntriesByDate(date)

    fun getTotalCaloriesForDate(date: LocalDate): Flow<Int?> =
        calorieEntryDao.getTotalForDate(date)

    suspend fun addCalorieEntry(calories: Int) {
        val now = LocalDateTime.now()
        calorieEntryDao.insert(
            CalorieEntry(
                calories = calories,
                date = now.toLocalDate(),
                timestamp = now
            )
        )
    }

    fun getLatestWeight(): Flow<WeightEntry?> =
        weightEntryDao.getLatestWeight()

    suspend fun addWeightEntry(weightKg: Float) {
        weightEntryDao.insert(
            WeightEntry(
                weightKg = weightKg,
                date = LocalDate.now()
            )
        )
    }

    val isFirstLaunch: Flow<Boolean> = userPreferences.isFirstLaunch
    val waterTarget: Flow<Int> = userPreferences.waterTargetMl
    val calorieTarget: Flow<Int> = userPreferences.calorieTarget
    val weightUnit: Flow<String> = userPreferences.weightUnit
    val isDarkMode: Flow<Boolean> = userPreferences.isDarkMode
    val heightCm: Flow<Int> = userPreferences.heightCm
    val gender: Flow<String> = userPreferences.gender

    suspend fun completeOnboarding(
        waterTargetMl: Int,
        calorieTarget: Int,
        initialWeight: Float,
        weightUnit: String,
        heightCm: Int,
        gender: String,
        targetWeightKg: Float,
        userName: String
    ) {
        userPreferences.setWaterTarget(waterTargetMl)
        userPreferences.setCalorieTarget(calorieTarget)
        userPreferences.setWeightUnit(weightUnit)
        userPreferences.setHeight(heightCm)
        userPreferences.setGender(gender)
        userPreferences.setTargetWeight(targetWeightKg)
        userPreferences.setUserName(userName)
        addWeightEntry(initialWeight)
        userPreferences.setFirstLaunchCompleted()
    }

    suspend fun updateTargets(waterTargetMl: Int, calorieTarget: Int, weightUnit: String) {
        userPreferences.setWaterTarget(waterTargetMl)
        userPreferences.setCalorieTarget(calorieTarget)
        userPreferences.setWeightUnit(weightUnit)
    }

    fun getAllWaterEntries(): Flow<List<WaterEntry>> = waterEntryDao.getAllEntries()
    fun getAllCalorieEntries(): Flow<List<CalorieEntry>> = calorieEntryDao.getAllEntries()
    fun getAllWeightEntries(): Flow<List<WeightEntry>> = weightEntryDao.getAllEntries()

    suspend fun addWaterEntryForDate(amountMl: Int, date: LocalDate, time: LocalDateTime) {
        waterEntryDao.insert(
            WaterEntry(
                amountMl = amountMl,
                timestamp = time,
                date = date.toString()
            )
        )
    }

    suspend fun addCalorieEntryForDate(calories: Int, date: LocalDate, time: LocalDateTime) {
        calorieEntryDao.insert(
            CalorieEntry(
                calories = calories,
                date = date,
                timestamp = time
            )
        )
    }

    suspend fun addWeightEntryForDate(weightKg: Float, date: LocalDate) {
        weightEntryDao.insert(
            WeightEntry(
                weightKg = weightKg,
                date = date
            )
        )
    }

    suspend fun deleteWaterEntry(entry: WaterEntry) = waterEntryDao.delete(entry)
    suspend fun deleteCalorieEntry(entry: CalorieEntry) = calorieEntryDao.delete(entry)
    suspend fun deleteWeightEntry(entry: WeightEntry) = weightEntryDao.delete(entry)

    suspend fun deleteEntriesByDate(date: LocalDate) {
        waterEntryDao.deleteByDate(date.toString())
        calorieEntryDao.deleteByDate(date)
        weightEntryDao.deleteByDate(date)
    }

    suspend fun toggleDarkMode(isDark: Boolean) {
        userPreferences.setDarkMode(isDark)
    }
}
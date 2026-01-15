package com.fitness.tracker.data.dao

import androidx.room.*
import com.fitness.tracker.data.entity.CalorieEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CalorieEntryDao {
    @Query("SELECT * FROM calorie_entries WHERE date = :date ORDER BY timestamp DESC")
    fun getEntriesByDate(date: LocalDate): Flow<List<CalorieEntry>>

    @Query("SELECT SUM(calories) FROM calorie_entries WHERE date = :date")
    fun getTotalForDate(date: LocalDate): Flow<Int?>

    @Insert
    suspend fun insert(entry: CalorieEntry)

    @Delete
    suspend fun delete(entry: CalorieEntry)

    @Query("DELETE FROM calorie_entries WHERE date = :date")
    suspend fun deleteByDate(date: LocalDate)

    @Query("SELECT * FROM calorie_entries ORDER BY date DESC, timestamp DESC")
    fun getAllEntries(): Flow<List<CalorieEntry>>
}
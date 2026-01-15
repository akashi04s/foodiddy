package com.fitness.tracker.data.dao

import androidx.room.*
import com.fitness.tracker.data.entity.WaterEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterEntryDao {
    @Query("SELECT * FROM water_entries WHERE date = :date ORDER BY timestamp DESC")
    fun getEntriesByDate(date: String): Flow<List<WaterEntry>>

    @Query("SELECT SUM(amountMl) FROM water_entries WHERE date = :date")
    fun getTotalForDate(date: String): Flow<Int?>

    @Insert
    suspend fun insert(entry: WaterEntry)

    @Delete
    suspend fun delete(entry: WaterEntry)

    @Query("DELETE FROM water_entries WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("SELECT DISTINCT date FROM water_entries ORDER BY date DESC")
    fun getAllDates(): Flow<List<String>>

    @Query("SELECT * FROM water_entries ORDER BY date DESC, timestamp DESC")
    fun getAllEntries(): Flow<List<WaterEntry>>
}
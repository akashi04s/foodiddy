package com.fitness.tracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "calorie_entries")
data class CalorieEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val calories: Int,
    val date: LocalDate,
    val timestamp: LocalDateTime
)
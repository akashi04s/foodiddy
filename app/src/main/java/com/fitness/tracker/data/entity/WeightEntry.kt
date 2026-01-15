package com.fitness.tracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey
    val date: LocalDate,
    val weightKg: Float
)
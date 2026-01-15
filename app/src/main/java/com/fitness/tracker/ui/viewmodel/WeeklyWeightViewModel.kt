package com.fitness.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.repository.FitnessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class WeeklyWeightData(
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val averageWeight: Float?,
    val weightChange: Float?
)

data class WeeklyWeightUiState(
    val weeklyData: List<WeeklyWeightData> = emptyList(),
    val weightUnit: String = "kg",
    val isLoading: Boolean = true
)

class WeeklyWeightViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyWeightUiState())
    val uiState: StateFlow<WeeklyWeightUiState> = _uiState.asStateFlow()

    init {
        loadWeeklyWeights()
    }

    private fun loadWeeklyWeights() {
        viewModelScope.launch {
            combine(
                repository.getAllWeightEntries(),
                repository.weightUnit
            ) { weightEntries, unit ->
                val weeks = weightEntries
                    .map { it.date }
                    .map { date ->
                        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    }
                    .distinct()
                    .sortedDescending()
                
                val weeklyData = weeks.mapIndexed { index, weekStart ->
                    val weekEnd = weekStart.plusDays(6)
                    
                    val weekWeights = weightEntries
                        .filter { it.date in weekStart..weekEnd }
                        .map { it.weightKg }
                    
                    val averageWeight = if (weekWeights.isNotEmpty()) {
                        weekWeights.average().toFloat()
                    } else null
                    
                    val previousWeekAverage = if (index < weeks.size - 1) {
                        val prevWeekStart = weeks[index + 1]
                        val prevWeekEnd = prevWeekStart.plusDays(6)
                        val prevWeights = weightEntries
                            .filter { it.date in prevWeekStart..prevWeekEnd }
                            .map { it.weightKg }
                        if (prevWeights.isNotEmpty()) prevWeights.average().toFloat() else null
                    } else null
                    
                    val weightChange = if (averageWeight != null && previousWeekAverage != null) {
                        previousWeekAverage - averageWeight
                    } else null
                    
                    WeeklyWeightData(
                        weekStart = weekStart,
                        weekEnd = weekEnd,
                        averageWeight = averageWeight,
                        weightChange = weightChange
                    )
                }
                
                WeeklyWeightUiState(
                    weeklyData = weeklyData,
                    weightUnit = unit,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
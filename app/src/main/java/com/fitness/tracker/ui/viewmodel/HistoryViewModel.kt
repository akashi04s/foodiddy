package com.fitness.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.entity.CalorieEntry
import com.fitness.tracker.data.entity.WaterEntry
import com.fitness.tracker.data.entity.WeightEntry
import com.fitness.tracker.data.repository.FitnessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyHistoryItem(
    val date: LocalDate,
    val waterTotal: Int,
    val waterTarget: Int,
    val calorieTotal: Int,
    val calorieTarget: Int,
    val weight: Float?,
    val weightChangeFromPrevious: Float?,
    val weightChangeFromFirst: Float?,
    val waterEntries: List<WaterEntry> = emptyList(),
    val calorieEntries: List<CalorieEntry> = emptyList(),
    val isToday: Boolean = false,
    val isEmpty: Boolean = false
)

data class HistoryUiState(
    val historyItems: List<DailyHistoryItem> = emptyList(),
    val isLoading: Boolean = true,
    val showDatePicker: Boolean = false,
    val showAddEntryDialog: Boolean = false,
    val selectedDate: LocalDate? = null,
    val showDeleteDialog: Boolean = false,
    val dateToDelete: LocalDate? = null
)

class HistoryViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            combine(
                repository.getAllWaterEntries(),
                repository.getAllCalorieEntries(),
                repository.getAllWeightEntries(),
                repository.waterTarget,
                repository.calorieTarget
            ) { waterEntries, calorieEntries, weightEntries, waterTarget, calorieTarget ->

                val today = LocalDate.now()
                
                val datesWithData = (waterEntries.map { LocalDate.parse(it.date) } +
                        calorieEntries.map { it.date } +
                        weightEntries.map { it.date })
                    .distinct()
                    .sorted()

                val dates = if (datesWithData.isNotEmpty()) {
                    val oldestDate = datesWithData.minOrNull()!!
                    val newestDate = datesWithData.maxOrNull()!!
                    
                    val allDates = mutableListOf<LocalDate>()
                    var currentDate = oldestDate
                    while (!currentDate.isAfter(newestDate)) {
                        allDates.add(currentDate)
                        currentDate = currentDate.plusDays(1)
                    }
                    allDates.sortedDescending()
                } else {
                    emptyList()
                }

                val firstWeight = weightEntries
                    .sortedBy { it.date }
                    .firstOrNull()

                dates.mapIndexed { index, date ->
                    val hasData = datesWithData.contains(date)
                    val isToday = date == today
                    val todayWaterEntries = if (isToday) {
                        waterEntries.filter { LocalDate.parse(it.date) == date }
                    } else emptyList()
                    
                    val todayCalorieEntries = if (isToday) {
                        calorieEntries.filter { it.date == date }
                    } else emptyList()
                    
                    val waterTotal = waterEntries
                        .filter { LocalDate.parse(it.date) == date }
                        .sumOf { it.amountMl }
                    
                    val calorieTotal = calorieEntries
                        .filter { it.date == date }
                        .sumOf { it.calories }
                    
                    val weight = weightEntries
                        .firstOrNull { it.date == date }
                        ?.weightKg

                    val previousDayWeight = if (index < dates.size - 1) {
                        weightEntries.firstOrNull { it.date == dates[index + 1] }?.weightKg
                    } else null

                    val weightChangeFromPrevious = if (weight != null && previousDayWeight != null) {
                        weight - previousDayWeight
                    } else null

                    val weightChangeFromFirst = if (weight != null && firstWeight != null) {
                        weight - firstWeight.weightKg
                    } else null

                    DailyHistoryItem(
                        date = date,
                        waterTotal = waterTotal,
                        waterTarget = waterTarget,
                        calorieTotal = calorieTotal,
                        calorieTarget = calorieTarget,
                        weight = weight,
                        weightChangeFromPrevious = weightChangeFromPrevious,
                        weightChangeFromFirst = weightChangeFromFirst,
                        waterEntries = todayWaterEntries,
                        calorieEntries = todayCalorieEntries,
                        isToday = isToday,
                        isEmpty = !hasData
                    )
                }
            }.collect { items ->
                _uiState.value = _uiState.value.copy(
                    historyItems = items,
                    isLoading = false
                )
            }
        }
    }

    fun showDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePicker = true)
    }

    fun hideDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePicker = false)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            showDatePicker = false,
            showAddEntryDialog = true,
            selectedDate = date
        )
    }

    fun hideAddEntryDialog() {
        _uiState.value = _uiState.value.copy(
            showAddEntryDialog = false,
            selectedDate = null
        )
    }

    fun saveHistoryEntry(water: Int, calories: Int, weight: Float, date: LocalDate) {
        viewModelScope.launch {
            try {
                // Add all three entries for the selected date with timestamp
                val timestamp = date.atTime(12, 0)
                repository.addWaterEntryForDate(water, date, timestamp)
                repository.addCalorieEntryForDate(calories, date, timestamp)
                repository.addWeightEntryForDate(weight, date)
                hideAddEntryDialog()
            } catch (e: Exception) {
            }
        }
    }

    fun requestDeleteHistory(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            dateToDelete = date
        )
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            dateToDelete = null
        )
    }

    fun confirmDeleteHistory() {
        viewModelScope.launch {
            try {
                _uiState.value.dateToDelete?.let { date ->
                    repository.deleteEntriesByDate(date)
                    hideDeleteDialog()
                }
            } catch (e: Exception) {
            }
        }
    }
}
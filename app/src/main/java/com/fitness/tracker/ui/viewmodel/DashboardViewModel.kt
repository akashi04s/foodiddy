package com.fitness.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.repository.FitnessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class DashboardUiState(
    val waterConsumed: Int = 0,
    val waterTarget: Int = 2000,
    val caloriesConsumed: Int = 0,
    val calorieTarget: Int = 2000,
    val currentWeight: Float? = null,
    val weightUnit: String = "kg",
    val heightCm: Int = 170,
    val gender: String = "male",
    val isLoading: Boolean = true,
    val showWaterDialog: Boolean = false,
    val showCalorieDialog: Boolean = false,
    val showWeightDialog: Boolean = false,
    val showTargetAdjustDialog: Boolean = false,
    val suggestedWaterTarget: Int? = null,
    val suggestedCalorieTarget: Int? = null
)

class DashboardViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val today = LocalDate.now()
        val todayString = today.toString()

        viewModelScope.launch {
            combine(
                repository.getTotalWaterForDate(todayString),
                repository.waterTarget,
                repository.getTotalCaloriesForDate(today),
                repository.calorieTarget,
                repository.getLatestWeight(),
                repository.weightUnit,
                repository.heightCm,
                repository.gender
            ) { flows: Array<Any?> ->
                val water = flows[0] as? Int
                val waterTarget = flows[1] as Int
                val calories = flows[2] as? Int
                val calorieTarget = flows[3] as Int
                val weight = flows[4] as? com.fitness.tracker.data.entity.WeightEntry
                val weightUnit = flows[5] as String
                val height = flows[6] as Int
                val gender = flows[7] as String
                
                DashboardUiState(
                    waterConsumed = water ?: 0,
                    waterTarget = waterTarget,
                    caloriesConsumed = calories ?: 0,
                    calorieTarget = calorieTarget,
                    currentWeight = weight?.weightKg,
                    weightUnit = weightUnit,
                    heightCm = height,
                    gender = gender,
                    isLoading = false,
                    showWaterDialog = _uiState.value.showWaterDialog,
                    showCalorieDialog = _uiState.value.showCalorieDialog,
                    showWeightDialog = _uiState.value.showWeightDialog,
                    showTargetAdjustDialog = _uiState.value.showTargetAdjustDialog,
                    suggestedWaterTarget = _uiState.value.suggestedWaterTarget,
                    suggestedCalorieTarget = _uiState.value.suggestedCalorieTarget
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun showWaterDialog() {
        _uiState.value = _uiState.value.copy(showWaterDialog = true)
    }

    fun hideWaterDialog() {
        _uiState.value = _uiState.value.copy(showWaterDialog = false)
    }

    fun showCalorieDialog() {
        _uiState.value = _uiState.value.copy(showCalorieDialog = true)
    }

    fun hideCalorieDialog() {
        _uiState.value = _uiState.value.copy(showCalorieDialog = false)
    }

    fun showWeightDialog() {
        _uiState.value = _uiState.value.copy(showWeightDialog = true)
    }

    fun hideWeightDialog() {
        _uiState.value = _uiState.value.copy(showWeightDialog = false)
    }

    private fun calculateBMI(weightKg: Float, heightCm: Int): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }

    private fun calculateTargetsFromBMI(weightKg: Float, heightCm: Int, gender: String): Pair<Int, Int> {
        val bmi = calculateBMI(weightKg, heightCm)
        
        val baseWater = when {
            bmi < 18.5 -> 2000
            bmi < 25 -> 2500
            bmi < 30 -> 3000
            else -> 3500
        }
        
        val baseCalories = when {
            bmi < 18.5 -> if (gender == "male") 2500 else 2200
            bmi < 25 -> if (gender == "male") 2200 else 1900
            bmi < 30 -> if (gender == "male") 2000 else 1700
            else -> if (gender == "male") 1800 else 1500
        }
        
        return Pair(baseWater, baseCalories)
    }

    fun addWeightEntry(weightKg: Float) {
        viewModelScope.launch {
            try {
                repository.addWeightEntry(weightKg)
                
                val currentState = _uiState.value
                val (suggestedWater, suggestedCalories) = calculateTargetsFromBMI(
                    weightKg,
                    currentState.heightCm,
                    currentState.gender
                )
                
                val waterDiff = kotlin.math.abs(suggestedWater - currentState.waterTarget)
                val calorieDiff = kotlin.math.abs(suggestedCalories - currentState.calorieTarget)
                
                if (waterDiff >= 500 || calorieDiff >= 250) {
                    _uiState.value = currentState.copy(
                        showWeightDialog = false,
                        showTargetAdjustDialog = true,
                        suggestedWaterTarget = suggestedWater,
                        suggestedCalorieTarget = suggestedCalories
                    )
                } else {
                    hideWeightDialog()
                }
            } catch (e: Exception) {
                hideWeightDialog()
            }
        }
    }

    fun hideTargetAdjustDialog() {
        _uiState.value = _uiState.value.copy(
            showTargetAdjustDialog = false,
            suggestedWaterTarget = null,
            suggestedCalorieTarget = null
        )
    }

    fun autoAdjustTargets() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                state.suggestedWaterTarget?.let { water ->
                    state.suggestedCalorieTarget?.let { calories ->
                        repository.updateTargets(water, calories, state.weightUnit)
                        hideTargetAdjustDialog()
                    }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun manualAdjustTargets() {
        hideTargetAdjustDialog()
    }

    fun addWaterEntry(amountMl: Int) {
        viewModelScope.launch {
            try {
                if (amountMl < 0) {
                    val currentWater = _uiState.value.waterConsumed
                    val newAmount = (currentWater + amountMl).coerceAtLeast(0)
                    val adjustment = newAmount - currentWater
                    if (adjustment != 0) {
                        repository.addWaterEntry(adjustment)
                    }
                } else {
                    repository.addWaterEntry(amountMl)
                }
                hideWaterDialog()
            } catch (e: Exception) {
            }
        }
    }

    fun addCalorieEntry(calories: Int) {
        viewModelScope.launch {
            try {
                if (calories < 0) {
                    val currentCalories = _uiState.value.caloriesConsumed
                    val newAmount = (currentCalories + calories).coerceAtLeast(0)
                    val adjustment = newAmount - currentCalories
                    if (adjustment != 0) {
                        repository.addCalorieEntry(adjustment)
                    }
                } else {
                    repository.addCalorieEntry(calories)
                }
                hideCalorieDialog()
            } catch (e: Exception) {
            }
        }
    }
}
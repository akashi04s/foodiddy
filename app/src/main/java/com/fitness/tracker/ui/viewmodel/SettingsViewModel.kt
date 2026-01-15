package com.fitness.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.repository.FitnessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val waterTarget: String = "2000",
    val calorieTarget: String = "2000",
    val weightUnit: String = "kg",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class SettingsViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                repository.waterTarget,
                repository.calorieTarget,
                repository.weightUnit
            ) { flows: Array<Any?> ->
                val waterTarget = flows[0] as Int
                val calorieTarget = flows[1] as Int
                val weightUnit = flows[2] as String
                
                SettingsUiState(
                    waterTarget = waterTarget.toString(),
                    calorieTarget = calorieTarget.toString(),
                    weightUnit = weightUnit,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun updateWaterTarget(value: String) {
        _uiState.value = _uiState.value.copy(
            waterTarget = value,
            successMessage = null,
            errorMessage = null
        )
    }

    fun updateCalorieTarget(value: String) {
        _uiState.value = _uiState.value.copy(
            calorieTarget = value,
            successMessage = null,
            errorMessage = null
        )
    }

    fun updateWeightUnit(unit: String) {
        _uiState.value = _uiState.value.copy(
            weightUnit = unit,
            successMessage = null,
            errorMessage = null
        )
    }

    fun saveSettings() {
        val state = _uiState.value

        // Validation
        val waterTarget = state.waterTarget.toIntOrNull()
        val calorieTarget = state.calorieTarget.toIntOrNull()

        if (waterTarget == null || waterTarget <= 0) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid water target")
            return
        }

        if (calorieTarget == null || calorieTarget <= 0) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid calorie target")
            return
        }

        _uiState.value = state.copy(isSaving = true, errorMessage = null, successMessage = null)

        viewModelScope.launch {
            try {
                repository.updateTargets(
                    waterTargetMl = waterTarget,
                    calorieTarget = calorieTarget,
                    weightUnit = state.weightUnit
                )
                _uiState.value = state.copy(
                    isSaving = false,
                    successMessage = "Settings saved successfully!"
                )
                
                // Clear success message after 2 seconds
                kotlinx.coroutines.delay(2000)
                _uiState.value = _uiState.value.copy(successMessage = null)
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isSaving = false,
                    errorMessage = "Error saving settings: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}
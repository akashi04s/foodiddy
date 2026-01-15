package com.fitness.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.tracker.data.repository.FitnessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow

data class OnboardingUiState(
    val currentStep: Int = 1,
    val userName: String = "",
    val gender: String = "",
    val heightCm: String = "",
    val currentWeight: String = "",
    val bmi: Float = 0f,
    val bmiCategory: String = "",
    val recommendedWater: Int = 2000,
    val recommendedCalories: Int = 2000,
    val recommendedWeight: Float = 70f,
    val waterTargetMl: String = "2000",
    val calorieTarget: String = "2000",
    val targetWeight: String = "70",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class OnboardingViewModel(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    // Step 1 - Name, Gender, Height & Weight Combined
    fun updateUserName(value: String) {
        _uiState.value = _uiState.value.copy(userName = value, errorMessage = null)
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender, errorMessage = null)
    }

    fun updateHeight(value: String) {
        _uiState.value = _uiState.value.copy(heightCm = value, errorMessage = null)
    }

    fun updateCurrentWeight(value: String) {
        _uiState.value = _uiState.value.copy(currentWeight = value, errorMessage = null)
    }

    fun nextFromStep1() {
        val state = _uiState.value

        if (state.userName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your name")
            return
        }

        if (state.gender.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please select your gender")
            return
        }

        val height = state.heightCm.toIntOrNull()
        if (height == null || height < 100 || height > 250) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid height (100-250 cm)")
            return
        }

        val weight = state.currentWeight.toFloatOrNull()
        if (weight == null || weight < 30 || weight > 300) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid weight (30-300 kg)")
            return
        }

        // Calculate BMI and recommendations
        val heightM = state.heightCm.toFloat() / 100
        val bmi = weight / (heightM.pow(2))
        val bmiCategory = getBMICategory(bmi)
        
        val recommendedWater = calculateRecommendedWater(weight)
        val recommendedCalories = calculateRecommendedCalories(weight, state.heightCm.toInt(), state.gender)
        val recommendedWeight = calculateIdealWeight(state.heightCm.toInt(), state.gender)

        _uiState.value = state.copy(
            currentStep = 2,
            bmi = bmi,
            bmiCategory = bmiCategory,
            recommendedWater = recommendedWater,
            recommendedCalories = recommendedCalories,
            recommendedWeight = recommendedWeight,
            waterTargetMl = recommendedWater.toString(),
            calorieTarget = recommendedCalories.toString(),
            targetWeight = String.format("%.1f", recommendedWeight),
            errorMessage = null
        )
    }

    fun updateWaterTarget(value: String) {
        _uiState.value = _uiState.value.copy(waterTargetMl = value, errorMessage = null)
    }

    fun updateCalorieTarget(value: String) {
        _uiState.value = _uiState.value.copy(calorieTarget = value, errorMessage = null)
    }

    fun updateTargetWeight(value: String) {
        _uiState.value = _uiState.value.copy(targetWeight = value, errorMessage = null)
    }

    fun previousStep() {
        val state = _uiState.value
        if (state.currentStep > 1) {
            _uiState.value = state.copy(currentStep = state.currentStep - 1, errorMessage = null)
        }
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validation
        val waterTarget = state.waterTargetMl.toIntOrNull()
        val calorieTarget = state.calorieTarget.toIntOrNull()
        val targetWeight = state.targetWeight.toFloatOrNull()
        val currentWeight = state.currentWeight.toFloatOrNull()

        if (waterTarget == null || waterTarget <= 0) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid water target")
            return
        }

        if (calorieTarget == null || calorieTarget <= 0) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid calorie target")
            return
        }

        if (targetWeight == null || targetWeight <= 0) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid target weight")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                repository.completeOnboarding(
                    waterTargetMl = waterTarget,
                    calorieTarget = calorieTarget,
                    initialWeight = currentWeight!!,
                    weightUnit = "kg",
                    heightCm = state.heightCm.toInt(),
                    gender = state.gender,
                    targetWeightKg = targetWeight,
                    userName = state.userName
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    private fun calculateRecommendedWater(weightKg: Float): Int {
        return (weightKg * 35).toInt()
    }

    private fun calculateRecommendedCalories(weightKg: Float, heightCm: Int, gender: String): Int {
        val age = 30
        val bmr = if (gender == "male") {
            (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
        } else {
            (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161
        }
        return (bmr * 1.2).toInt()
    }

    private fun calculateIdealWeight(heightCm: Int, gender: String): Float {
        val heightInches = heightCm / 2.54
        val inchesOver5Feet = heightInches - 60

        return if (gender == "male") {
            (52 + (1.9 * inchesOver5Feet)).toFloat()
        } else {
            (49 + (1.7 * inchesOver5Feet)).toFloat()
        }
    }
}
package com.fitness.tracker.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitness.tracker.R
import com.fitness.tracker.ui.viewmodel.OnboardingViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onOnboardingComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    if (uiState.currentStep > 1) {
                        IconButton(onClick = { viewModel.previousStep() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState.currentStep) {
            1 -> OnboardingStep1(viewModel, uiState, paddingValues)
            2 -> OnboardingStep2(viewModel, uiState, paddingValues, onOnboardingComplete)
        }
    }
}

// Step 1: Name, Gender, Height & Weight (Combined)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingStep1(
    viewModel: OnboardingViewModel,
    uiState: com.fitness.tracker.ui.viewmodel.OnboardingUiState,
    paddingValues: PaddingValues
) {
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "FooDiddy Logo",
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF494C65),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.userName,
                onValueChange = { viewModel.updateUserName(it) },
                label = { Text("Your Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("What should we call you?") }
            )
    
            Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Your Gender",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val maleSize by animateDpAsState(
                    targetValue = if (uiState.gender == "male") 140.dp else 120.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "maleSize"
                )
                val maleAlpha by animateFloatAsState(
                    targetValue = if (uiState.gender == "female") 0.4f else 1f,
                    animationSpec = tween(durationMillis = 300),
                    label = "maleAlpha"
                )
                
                Image(
                    painter = painterResource(id = R.drawable.male),
                    contentDescription = "Male",
                    modifier = Modifier
                        .size(maleSize)
                        .alpha(maleAlpha)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { viewModel.updateGender("male") }
                )

                val femaleSize by animateDpAsState(
                    targetValue = if (uiState.gender == "female") 140.dp else 120.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "femaleSize"
                )
                val femaleAlpha by animateFloatAsState(
                    targetValue = if (uiState.gender == "male") 0.4f else 1f,
                    animationSpec = tween(durationMillis = 300),
                    label = "femaleAlpha"
                )
                
                Image(
                    painter = painterResource(id = R.drawable.female),
                    contentDescription = "Female",
                    modifier = Modifier
                        .size(femaleSize)
                        .alpha(femaleAlpha)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { viewModel.updateGender("female") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.heightCm,
                onValueChange = { viewModel.updateHeight(it) },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("Enter your height in centimeters") }
            )

            OutlinedTextField(
                value = uiState.currentWeight,
                onValueChange = { viewModel.updateCurrentWeight(it) },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("Enter your current weight") }
            )

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.nextFromStep1() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Next", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// Step 2: BMI & Recommendations
@Composable
fun OnboardingStep2(
    viewModel: OnboardingViewModel,
    uiState: com.fitness.tracker.ui.viewmodel.OnboardingUiState,
    paddingValues: PaddingValues,
    onOnboardingComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "FooDiddy Logo",
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF494C65),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val cardAlphaAnim = remember { Animatable(0f) }
            val cardOffsetYAnim = remember { Animatable(50f) }
            val textAlphaAnim = remember { Animatable(0f) }

            LaunchedEffect(Unit) {
                launch {
                    cardAlphaAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    )
                }
                launch {
                    cardOffsetYAnim.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    )
                }
                delay(200)
                textAlphaAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(cardAlphaAnim.value)
                    .offset(y = cardOffsetYAnim.value.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDA935D)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).alpha(textAlphaAnim.value),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your BMI",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format(java.util.Locale.US, "%.1f", uiState.bmi),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = uiState.bmiCategory,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }

        Text(
            text = "Recommended Daily Goals",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "You can edit these values",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

            OutlinedTextField(
                value = uiState.waterTargetMl,
                onValueChange = { viewModel.updateWaterTarget(it) },
                label = { Text("Daily Water Goal (ml)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("Recommended: ${uiState.recommendedWater} ml") }
            )

            OutlinedTextField(
                value = uiState.calorieTarget,
                onValueChange = { viewModel.updateCalorieTarget(it) },
                label = { Text("Daily Calorie Goal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("Recommended: ${uiState.recommendedCalories} cal") }
            )

            OutlinedTextField(
                value = uiState.targetWeight,
                onValueChange = { viewModel.updateTargetWeight(it) },
                label = { Text("Target Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                ),
                supportingText = { Text("Ideal weight: ${String.format(java.util.Locale.US, "%.1f", uiState.recommendedWeight)} kg") }
            )

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.completeOnboarding(onOnboardingComplete) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Get Started", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
package com.fitness.tracker.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitness.tracker.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = com.fitness.tracker.R.drawable.logo),
                        contentDescription = "FooDiddy",
                        modifier = Modifier.height(36.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Edit Your Daily Goals",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = bahnschriftFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.waterTarget,
                    onValueChange = { viewModel.updateWaterTarget(it) },
                    label = { Text("Daily Water Target (ml)", fontFamily = bahnschriftFont) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1A1C1E),
                        focusedContainerColor = Color(0xFF1A1C1E),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedBorderColor = Color(0xFFA01B37),
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedLabelColor = Color(0xFFA01B37)
                    ),
                    supportingText = {
                        Text("Recommended: 2000-3000 ml", fontFamily = bahnschriftFont, color = Color.White.copy(alpha = 0.6f))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = null,
                            tint = Color(0xFFA01B37)
                        )
                    }
                )

                OutlinedTextField(
                    value = uiState.calorieTarget,
                    onValueChange = { viewModel.updateCalorieTarget(it) },
                    label = { Text("Daily Calorie Target", fontFamily = bahnschriftFont) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF1A1C1E),
                        focusedContainerColor = Color(0xFF1A1C1E),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedBorderColor = Color(0xFF228B6F),
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedLabelColor = Color(0xFF228B6F)
                    ),
                    supportingText = {
                        Text("Average: 2000-2500 calories", fontFamily = bahnschriftFont, color = Color.White.copy(alpha = 0.6f))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Color(0xFF228B6F)
                        )
                    }
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Weight Unit",
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = bahnschriftFont,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val kgSize by animateDpAsState(
                            targetValue = if (uiState.weightUnit == "kg") 160.dp else 140.dp,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "kgSize"
                        )
                        val kgAlpha by animateFloatAsState(
                            targetValue = if (uiState.weightUnit == "lbs") 0.4f else 1f,
                            animationSpec = tween(durationMillis = 300),
                            label = "kgAlpha"
                        )
                        
                        Surface(
                            modifier = Modifier
                                .width(kgSize)
                                .height(56.dp)
                                .alpha(kgAlpha)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { viewModel.updateWeightUnit("kg") },
                            shape = RoundedCornerShape(28.dp),
                            color = if (uiState.weightUnit == "kg")
                                Color.White.copy(alpha = 0.2f)
                            else
                                Color(0xFF1A1C1E),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = if (uiState.weightUnit == "kg") 0.5f else 0.3f)
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "Kilograms (kg)",
                                    fontFamily = bahnschriftFont,
                                    fontWeight = if (uiState.weightUnit == "kg") FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        val lbsSize by animateDpAsState(
                            targetValue = if (uiState.weightUnit == "lbs") 160.dp else 140.dp,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "lbsSize"
                        )
                        val lbsAlpha by animateFloatAsState(
                            targetValue = if (uiState.weightUnit == "kg") 0.4f else 1f,
                            animationSpec = tween(durationMillis = 300),
                            label = "lbsAlpha"
                        )
                        
                        Surface(
                            modifier = Modifier
                                .width(lbsSize)
                                .height(56.dp)
                                .alpha(lbsAlpha)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { viewModel.updateWeightUnit("lbs") },
                            shape = RoundedCornerShape(28.dp),
                            color = if (uiState.weightUnit == "lbs")
                                Color.White.copy(alpha = 0.2f)
                            else
                                Color(0xFF1A1C1E),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = if (uiState.weightUnit == "lbs") 0.5f else 0.3f)
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "Pounds (lbs)",
                                    fontFamily = bahnschriftFont,
                                    fontWeight = if (uiState.weightUnit == "lbs") FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                if (uiState.successMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1C1E)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color(0xFF228B6F)
                            )
                            Text(
                                text = uiState.successMessage!!,
                                fontFamily = bahnschriftFont,
                                color = Color.White
                            )
                        }
                    }
                }

                if (uiState.errorMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1A1C1E)
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
                                text = uiState.errorMessage!!,
                                fontFamily = bahnschriftFont,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.saveSettings() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White,
                        disabledContainerColor = Color.White.copy(alpha = 0.1f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    )
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Save Changes", style = MaterialTheme.typography.titleMedium, fontFamily = bahnschriftFont)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
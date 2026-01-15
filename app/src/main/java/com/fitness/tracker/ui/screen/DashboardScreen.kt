package com.fitness.tracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import com.fitness.tracker.ui.viewmodel.DashboardViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 8.dp)
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val dayOfWeek = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = dayOfWeek,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                fontFamily = bahnschriftFont,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val weightInteractionSource = remember { MutableInteractionSource() }
                        val isWeightPressed by weightInteractionSource.collectIsPressedAsState()
                        val weightScale by animateFloatAsState(
                            targetValue = if (isWeightPressed) 0.92f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            label = "weight_scale"
                        )
                        
                        Box(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = weightInteractionSource,
                                    indication = null
                                ) { viewModel.showWeightDialog() }
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                                .graphicsLayer {
                                    scaleX = weightScale
                                    scaleY = weightScale
                                }
                        ) {
                            Text(
                                text = if (uiState.currentWeight != null)
                                    String.format("%.1f %s", uiState.currentWeight, uiState.weightUnit).uppercase()
                                else "--",
                                style = MaterialTheme.typography.displayLarge,
                                fontFamily = bahnschriftFont,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = (-28).dp, y = (-7).dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    val waterProgress = (uiState.waterConsumed.toFloat() / uiState.waterTarget.toFloat()).coerceIn(0f, 1f)
                                    val startColor = Color(0xFFC9B9BD)
                                    val endColor = Color(0xFFA01B37)
                                    val waterColor = lerp(startColor, endColor, waterProgress)
                                    
                                    CircularProgressIndicator(
                                        progress = 1f,
                                        modifier = Modifier.size(220.dp),
                                        color = Color(0xFFCABABE),
                                        strokeWidth = 24.75.dp,
                                        strokeCap = StrokeCap.Round
                                    )
                                    CircularProgressIndicator(
                                        progress = waterProgress,
                                        modifier = Modifier.size(220.dp),
                                        color = waterColor,
                                        strokeWidth = 24.75.dp,
                                        strokeCap = StrokeCap.Round
                                    )
                                    
                                    val waterInteractionSource = remember { MutableInteractionSource() }
                                    val isWaterPressed by waterInteractionSource.collectIsPressedAsState()
                                    val waterScale by animateFloatAsState(
                                        targetValue = if (isWaterPressed) 0.88f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        ),
                                        label = "water_scale"
                                    )
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clickable(
                                                interactionSource = waterInteractionSource,
                                                indication = null
                                            ) { viewModel.showWaterDialog() }
                                            .graphicsLayer {
                                                scaleX = waterScale
                                                scaleY = waterScale
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            val waterPercentage = ((uiState.waterConsumed.toFloat() / uiState.waterTarget.toFloat()) * 100).toInt()
                                            Text(
                                                text = "$waterPercentage%",
                                                fontFamily = bahnschriftFont,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = "${uiState.waterConsumed} ml",
                                                fontFamily = bahnschriftFont,
                                                fontSize = 18.sp,
                                                color = Color.White.copy(alpha = 0.8f),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = 28.dp, y = 177.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    val calorieProgress = (uiState.caloriesConsumed.toFloat() / uiState.calorieTarget.toFloat()).coerceIn(0f, 1f)
                                    val startColor = Color(0xFFBBC7C2)
                                    val endColor = Color(0xFF228B6F)
                                    val calorieColor = lerp(startColor, endColor, calorieProgress)
                                    
                                    CircularProgressIndicator(
                                        progress = 1f,
                                        modifier = Modifier.size(220.dp),
                                        color = Color(0xFFBBC7C2),
                                        strokeWidth = 24.75.dp,
                                        strokeCap = StrokeCap.Round
                                    )
                                    CircularProgressIndicator(
                                        progress = calorieProgress,
                                        modifier = Modifier.size(220.dp),
                                        color = calorieColor,
                                        strokeWidth = 24.75.dp,
                                        strokeCap = StrokeCap.Round
                                    )
                                    
                                    val calorieInteractionSource = remember { MutableInteractionSource() }
                                    val isCaloriePressed by calorieInteractionSource.collectIsPressedAsState()
                                    val calorieScale by animateFloatAsState(
                                        targetValue = if (isCaloriePressed) 0.88f else 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        ),
                                        label = "calorie_scale"
                                    )
                                    
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clickable(
                                                interactionSource = calorieInteractionSource,
                                                indication = null
                                            ) { viewModel.showCalorieDialog() }
                                            .graphicsLayer {
                                                scaleX = calorieScale
                                                scaleY = calorieScale
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            val caloriePercentage = ((uiState.caloriesConsumed.toFloat() / uiState.calorieTarget.toFloat()) * 100).toInt()
                                            Text(
                                                text = "$caloriePercentage%",
                                                fontFamily = bahnschriftFont,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = "${uiState.caloriesConsumed} cal",
                                                fontFamily = bahnschriftFont,
                                                fontSize = 18.sp,
                                                color = Color.White.copy(alpha = 0.8f),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (-28).dp, y = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "TODAY'S PROGRESS",
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                fontFamily = bahnschriftFont,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                    }
                }
            }
        }

        if (uiState.showWaterDialog) {
            AddWaterDialog(
                onDismiss = { viewModel.hideWaterDialog() },
                onAdd = { amount -> viewModel.addWaterEntry(amount) }
            )
        }

        if (uiState.showCalorieDialog) {
            AddCalorieDialog(
                onDismiss = { viewModel.hideCalorieDialog() },
                onAdd = { calories -> viewModel.addCalorieEntry(calories) }
            )
        }

        if (uiState.showWeightDialog) {
            AddWeightDialog(
                currentWeight = uiState.currentWeight,
                onDismiss = { viewModel.hideWeightDialog() },
                onAdd = { weight -> viewModel.addWeightEntry(weight) }
            )
        }

        if (uiState.showTargetAdjustDialog &&
            uiState.suggestedWaterTarget != null &&
            uiState.suggestedCalorieTarget != null) {
            TargetAdjustmentDialog(
                currentWaterTarget = uiState.waterTarget,
                currentCalorieTarget = uiState.calorieTarget,
                suggestedWaterTarget = uiState.suggestedWaterTarget!!,
                suggestedCalorieTarget = uiState.suggestedCalorieTarget!!,
                onAutoAdjust = { viewModel.autoAdjustTargets() },
                onManualAdjust = { viewModel.manualAdjustTargets() },
                onCancel = { viewModel.hideTargetAdjustDialog() }
            )
        }
    }
}
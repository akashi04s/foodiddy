package com.fitness.tracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun TargetAdjustmentDialog(
    currentWaterTarget: Int,
    currentCalorieTarget: Int,
    suggestedWaterTarget: Int,
    suggestedCalorieTarget: Int,
    onAutoAdjust: () -> Unit,
    onManualAdjust: () -> Unit,
    onCancel: () -> Unit
) {
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1C1E)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Target Adjustment Suggested",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Based on your updated weight and BMI, we recommend adjusting your daily targets:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Water",
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = bahnschriftFont,
                                color = Color(0xFFA01B37)
                            )
                            Text(
                                "$currentWaterTarget → $suggestedWaterTarget ml",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = bahnschriftFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Calories",
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = bahnschriftFont,
                                color = Color(0xFF228B6F)
                            )
                            Text(
                                "$currentCalorieTarget → $suggestedCalorieTarget cal",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = bahnschriftFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onAutoAdjust,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Auto Adjust",
                                fontFamily = bahnschriftFont,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onManualAdjust,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Adjust Manually",
                                fontFamily = bahnschriftFont,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            "Cancel",
                            fontFamily = bahnschriftFont,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
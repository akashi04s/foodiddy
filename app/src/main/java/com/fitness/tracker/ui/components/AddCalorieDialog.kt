package com.fitness.tracker.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddCalorieDialog(
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit
) {
    var calories by remember { mutableStateOf("") }
    var selectedQuick by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = onDismiss,
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
                    .align(androidx.compose.ui.Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1C1E)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Add Calorie Intake",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = {
                            calories = it
                            selectedQuick = null
                            errorMessage = null
                        },
                        label = { Text("Calories", fontFamily = bahnschriftFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        isError = errorMessage != null,
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
                            if (errorMessage != null) {
                                Text(errorMessage!!, fontFamily = bahnschriftFont, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = Color(0xFF228B6F)
                            )
                        }
                    )

                    Text(
                        "Quick Add:",
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("200" to "200", "500" to "500", "1000" to "1000").forEach { (value, label) ->
                            OutlinedButton(
                                onClick = {
                                    calories = value
                                    selectedQuick = value
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedQuick == value) Color(0xFF228B6F).copy(alpha = 0.3f) else Color.Transparent,
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                            ) {
                                Text(label, fontFamily = bahnschriftFont)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", fontFamily = bahnschriftFont, color = Color.White)
                        }
                        Button(
                            onClick = {
                                val calorieInt = calories.toIntOrNull()
                                if (calorieInt == null || calorieInt == 0) {
                                    errorMessage = "Please enter a valid amount"
                                } else {
                                    onAdd(calorieInt)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Add", fontFamily = bahnschriftFont)
                        }
                    }
                }
            }
        }
    }
}
package com.fitness.tracker.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
    )
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        
                        if (selectedDate.isBefore(LocalDate.now())) {
                            onDateSelected(selectedDate)
                        }
                    }
                }
            ) {
                Text("OK", fontFamily = bahnschriftFont, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = bahnschriftFont, color = Color.White)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF1A1C1E)
        )
    ) {
        DatePicker(
            state = datePickerState,
            title = { Text("Select Past Date", fontFamily = bahnschriftFont, color = Color.White, modifier = Modifier.padding(16.dp)) },
            headline = {
                Text(
                    text = "Choose a date to add history",
                    fontFamily = bahnschriftFont,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            dateValidator = { millis ->
                val date = java.time.Instant.ofEpochMilli(millis)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                date.isBefore(LocalDate.now())
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF1A1C1E),
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.White,
                subheadContentColor = Color.White,
                yearContentColor = Color.White,
                currentYearContentColor = Color.White,
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = Color.White.copy(alpha = 0.2f),
                dayContentColor = Color.White,
                selectedDayContentColor = Color(0xFF1A1C1E),
                selectedDayContainerColor = Color.White,
                todayContentColor = Color.White,
                todayDateBorderColor = Color.White
            )
        )
    }
}

@Composable
fun AddHistoryEntryDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onSave: (Int, Int, Float) -> Unit
) {
    var water by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

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
                        text = "Add Entry for ${date.format(dateFormatter)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = water,
                        onValueChange = {
                            water = it
                            errorMessage = null
                        },
                        label = { Text("Water (ml)", fontFamily = bahnschriftFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFF1A1C1E),
                            focusedContainerColor = Color(0xFF1A1C1E),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = Color(0xFF3B7EA1),
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            focusedLabelColor = Color(0xFF3B7EA1)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocalDrink,
                                contentDescription = null,
                                tint = Color(0xFF3B7EA1)
                            )
                        }
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = {
                            calories = it
                            errorMessage = null
                        },
                        label = { Text("Calories", fontFamily = bahnschriftFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFF1A1C1E),
                            focusedContainerColor = Color(0xFF1A1C1E),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = Color(0xFF7A9B3D),
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            focusedLabelColor = Color(0xFF7A9B3D)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = Color(0xFF7A9B3D)
                            )
                        }
                    )

                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            weight = it
                            errorMessage = null
                        },
                        label = { Text("Weight (kg)", fontFamily = bahnschriftFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFF1A1C1E),
                            focusedContainerColor = Color(0xFF1A1C1E),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            focusedLabelColor = Color.White
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MonitorWeight,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            fontFamily = bahnschriftFont,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
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
                                val waterInt = water.toIntOrNull()
                                val caloriesInt = calories.toIntOrNull()
                                val weightFloat = weight.toFloatOrNull()

                                when {
                                    waterInt == null || waterInt <= 0 -> {
                                        errorMessage = "Please enter valid water amount"
                                    }
                                    caloriesInt == null || caloriesInt <= 0 -> {
                                        errorMessage = "Please enter valid calories"
                                    }
                                    weightFloat == null || weightFloat <= 0 -> {
                                        errorMessage = "Please enter valid weight"
                                    }
                                    else -> {
                                        onSave(waterInt, caloriesInt, weightFloat)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Save", fontFamily = bahnschriftFont)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteHistoryDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

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
                        text = "Delete History",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )

                    Text(
                        text = "Are you sure you want to delete all entries for ${date.format(dateFormatter)}?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )

                    Text(
                        text = "This action cannot be undone.",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = bahnschriftFont,
                        color = MaterialTheme.colorScheme.error
                    )

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
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Delete", fontFamily = bahnschriftFont)
                        }
                    }
                }
            }
        }
    }
}
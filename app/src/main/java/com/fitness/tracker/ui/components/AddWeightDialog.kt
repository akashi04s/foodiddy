package com.fitness.tracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddWeightDialog(
    currentWeight: Float?,
    onDismiss: () -> Unit,
    onAdd: (Float) -> Unit
) {
    var weight by remember(currentWeight) { mutableStateOf(currentWeight?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

    LaunchedEffect(currentWeight) {
        weight = currentWeight?.toString() ?: ""
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
                        "Update Weight",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = weight,
                        onValueChange = {
                            weight = it
                            errorMessage = null
                        },
                        label = { Text("Weight (kg)", fontFamily = bahnschriftFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        isError = errorMessage != null,
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
                        supportingText = {
                            if (errorMessage != null) {
                                Text(errorMessage!!, fontFamily = bahnschriftFont, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MonitorWeight,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
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
                            onClick = {
                                val weightFloat = weight.toFloatOrNull()
                                if (weightFloat == null || weightFloat <= 0) {
                                    errorMessage = "Please enter a valid weight"
                                } else {
                                    onAdd(weightFloat)
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
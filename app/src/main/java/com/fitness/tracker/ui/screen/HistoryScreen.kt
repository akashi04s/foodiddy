package com.fitness.tracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitness.tracker.ui.viewmodel.HistoryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showDatePicker() },
                containerColor = androidx.compose.ui.graphics.Color(0xFF3F4F63),
                contentColor = androidx.compose.ui.graphics.Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add History Entry")
            }
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
        } else if (uiState.historyItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "No history entries yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Tap the + button to add a history entry",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.historyItems) { item ->
                    HistoryDayCard(
                        item = item,
                        onLongPress = { viewModel.requestDeleteHistory(item.date) }
                    )
                }
            }
        }

        if (uiState.showDatePicker) {
            DatePickerDialog(
                onDismiss = { viewModel.hideDatePicker() },
                onDateSelected = { date -> viewModel.onDateSelected(date) }
            )
        }

        if (uiState.showAddEntryDialog && uiState.selectedDate != null) {
            AddHistoryEntryDialog(
                date = uiState.selectedDate!!,
                onDismiss = { viewModel.hideAddEntryDialog() },
                onSave = { water, calories, weight ->
                    viewModel.saveHistoryEntry(water, calories, weight, uiState.selectedDate!!)
                }
            )
        }

        if (uiState.showDeleteDialog && uiState.dateToDelete != null) {
            DeleteHistoryDialog(
                date = uiState.dateToDelete!!,
                onDismiss = { viewModel.hideDeleteDialog() },
                onConfirm = { viewModel.confirmDeleteHistory() }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryDayCard(
    item: com.fitness.tracker.ui.viewmodel.DailyHistoryItem,
    onLongPress: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { if (!item.isToday) onLongPress() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFF1A1C1E)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (item.isToday) "Today" else item.date.format(dateFormatter),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = bahnschriftFont,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (item.isEmpty) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = bahnschriftFont,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else if (item.isToday && (item.waterEntries.isNotEmpty() || item.calorieEntries.isNotEmpty())) {
                Text(
                    "Water Entries:",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = bahnschriftFont,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                item.waterEntries.forEach { entry ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${entry.amountMl} ml",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = bahnschriftFont
                        )
                        Text(
                            text = entry.timestamp.format(timeFormatter),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = bahnschriftFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    "Calorie Entries:",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = bahnschriftFont,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                item.calorieEntries.forEach { entry ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${entry.calories} cal",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = bahnschriftFont
                        )
                        Text(
                            text = entry.timestamp.format(timeFormatter),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = bahnschriftFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                
                Text(
                    "Daily Totals:",
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = bahnschriftFont,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else if (!item.isEmpty) {
            }

            if (!item.isEmpty) {
                Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Water",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${item.waterTotal}/${item.waterTarget} ml",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Calories",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${item.calorieTotal}/${item.calorieTarget} cal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Weight",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (item.weight != null) {
                        Text(
                            text = "%.1f kg".format(item.weight),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = bahnschriftFont,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "--",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = bahnschriftFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                }
            }
        }
    }
}
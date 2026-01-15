package com.fitness.tracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitness.tracker.ui.viewmodel.WeeklyWeightViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyWeightScreen(
    viewModel: WeeklyWeightViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val bahnschriftFont = FontFamily(Font(com.fitness.tracker.R.font.bahnschrift))

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
                    containerColor = Color.Transparent
                )
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
        } else if (uiState.weeklyData.isEmpty()) {
            // Empty state
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
                        text = "No weight data yet",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = bahnschriftFont,
                        color = Color.White
                    )
                    Text(
                        text = "Add weight entries to see weekly averages",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        color = Color.White.copy(alpha = 0.7f)
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
                // Title
                item {
                    Text(
                        text = "Average Weekly Weight",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(uiState.weeklyData) { weekData ->
                    WeeklyWeightCard(
                        weekData = weekData,
                        weightUnit = uiState.weightUnit,
                        bahnschriftFont = bahnschriftFont
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyWeightCard(
    weekData: com.fitness.tracker.ui.viewmodel.WeeklyWeightData,
    weightUnit: String,
    bahnschriftFont: FontFamily
) {
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1C1E)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Date range
            Text(
                text = "${weekData.weekStart.format(dateFormatter)} to ${weekData.weekEnd.format(dateFormatter)}",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = bahnschriftFont,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Weight and change in same row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Average weight
                if (weekData.averageWeight != null) {
                    Text(
                        text = "%.1f %s".format(weekData.averageWeight, weightUnit.uppercase()),
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = bahnschriftFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "No data",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = bahnschriftFont,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                
                // Weight change from previous week
                if (weekData.weightChange != null) {
                    val changeText = if (weekData.weightChange > 0) {
                        "%.1f %s less than last week".format(weekData.weightChange, weightUnit.uppercase())
                    } else {
                        "%.1f %s more than last week".format(-weekData.weightChange, weightUnit.uppercase())
                    }
                    
                    Text(
                        text = changeText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = bahnschriftFont,
                        color = if (weekData.weightChange > 0) 
                            Color(0xFF228B6F) // Green for weight loss
                        else 
                            Color(0xFFA01B37), // Red for weight gain
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
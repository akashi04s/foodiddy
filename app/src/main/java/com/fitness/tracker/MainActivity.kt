package com.fitness.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitness.tracker.ui.navigation.Screen
import com.fitness.tracker.ui.screen.DashboardScreen
import com.fitness.tracker.ui.screen.HistoryScreen
import com.fitness.tracker.ui.screen.OnboardingScreen
import com.fitness.tracker.ui.screen.SettingsScreen
import com.fitness.tracker.ui.screen.WeeklyWeightScreen
import com.fitness.tracker.ui.theme.FitnessTrackerTheme
import com.fitness.tracker.ui.viewmodel.DashboardViewModel
import com.fitness.tracker.ui.viewmodel.HistoryViewModel
import com.fitness.tracker.ui.viewmodel.OnboardingViewModel
import com.fitness.tracker.ui.viewmodel.SettingsViewModel
import com.fitness.tracker.ui.viewmodel.WeeklyWeightViewModel
import com.fitness.tracker.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as FitnessApplication).repository
        val viewModelFactory = ViewModelFactory(repository)

        setContent {
            val scope = rememberCoroutineScope()

            FitnessTrackerTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isFirstLaunch by remember { mutableStateOf<Boolean?>(null) }

                    LaunchedEffect(Unit) {
                        scope.launch {
                            isFirstLaunch = repository.isFirstLaunch.first()
                        }
                    }

                    when (isFirstLaunch) {
                        null -> {
                        }
                        true -> {
                            val onboardingViewModel: OnboardingViewModel = viewModel(
                                factory = viewModelFactory
                            )
                            OnboardingScreen(
                                viewModel = onboardingViewModel,
                                onOnboardingComplete = {
                                    isFirstLaunch = false
                                }
                            )
                        }
                        false -> {
                            MainScreenWithNavigation(
                                viewModelFactory = viewModelFactory
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithNavigation(
    viewModelFactory: ViewModelFactory
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        Screen.WeeklyWeight to Icons.Default.CalendarMonth,
        Screen.History to Icons.Default.History,
        Screen.Dashboard to Icons.Default.Home,
        Screen.Settings to Icons.Default.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ) {
                items.forEach { (screen, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.WeeklyWeight.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                }
            ) {
                val weeklyWeightViewModel: WeeklyWeightViewModel = viewModel(
                    factory = viewModelFactory
                )
                WeeklyWeightScreen(
                    viewModel = weeklyWeightViewModel
                )
            }
            composable(
                route = Screen.History.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { if (initialState.destination.route == Screen.WeeklyWeight.route) it else -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { if (targetState.destination.route == Screen.WeeklyWeight.route) it else -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                }
            ) {
                val historyViewModel: HistoryViewModel = viewModel(
                    factory = viewModelFactory
                )
                HistoryScreen(viewModel = historyViewModel)
            }
            composable(
                route = Screen.Dashboard.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = {
                            when (initialState.destination.route) {
                                Screen.WeeklyWeight.route, Screen.History.route -> it
                                else -> -it
                            }
                        },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = {
                            when (targetState.destination.route) {
                                Screen.WeeklyWeight.route, Screen.History.route -> it
                                else -> -it
                            }
                        },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                }
            ) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = viewModelFactory
                )
                DashboardScreen(viewModel = dashboardViewModel)
            }
            composable(
                route = Screen.Settings.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
                }
            ) {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = viewModelFactory
                )
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}
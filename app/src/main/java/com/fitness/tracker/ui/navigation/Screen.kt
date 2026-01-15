package com.fitness.tracker.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object WeeklyWeight : Screen("weekly_weight", "Weekly")
    object History : Screen("history", "History")
    object Dashboard : Screen("dashboard", "Dashboard")
    object Settings : Screen("settings", "Settings")
}
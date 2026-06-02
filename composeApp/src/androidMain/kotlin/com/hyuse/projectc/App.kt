package com.hyuse.projectc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyuse.projectc.navigation.NavGraph
import com.hyuse.projectc.navigation.Routes
import com.hyuse.projectc.ui.components.LucidBottomNavigation
import com.hyuse.projectc.ui.theme.ProjectCTheme

@Composable
fun App() {
    ProjectCTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val topLevelRoutes = listOf(
            Routes.HOME,
            Routes.EXPENSES_DASHBOARD,
            Routes.UTILITIES,
            Routes.REMINDERS_DASHBOARD,
            Routes.PROFILE
        )

        val shouldShowBottomBar = currentRoute in topLevelRoutes

        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar) {
                    LucidBottomNavigation(navController = navController)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavGraph(navController = navController)
            }
        }
    }
}
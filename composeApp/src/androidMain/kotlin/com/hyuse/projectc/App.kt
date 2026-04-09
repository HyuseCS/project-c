package com.hyuse.projectc

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.hyuse.projectc.navigation.NavGraph
import com.hyuse.projectc.ui.theme.ProjectCTheme

@Composable
fun App() {
    ProjectCTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController)
    }
}
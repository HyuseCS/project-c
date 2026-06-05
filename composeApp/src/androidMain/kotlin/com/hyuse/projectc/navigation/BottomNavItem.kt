package com.hyuse.projectc.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(Routes.HOME, Icons.Default.Home, "Home")
    object Expenses : BottomNavItem(Routes.EXPENSES_DASHBOARD, Icons.Default.AccountBalanceWallet, "Expenses")
    object Reminders : BottomNavItem(Routes.REMINDERS_DASHBOARD, Icons.Default.LocationOn, "Reminders")
    object Utilities : BottomNavItem(Routes.UTILITIES, Icons.Default.Build, "Utilities")
    object Profile : BottomNavItem(Routes.PROFILE, Icons.Default.Person, "Profile")
}

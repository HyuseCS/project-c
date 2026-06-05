package com.hyuse.projectc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyuse.projectc.navigation.NavGraph
import com.hyuse.projectc.navigation.Routes
import com.hyuse.projectc.ui.components.LucidBottomNavigation
import com.hyuse.projectc.ui.theme.ProjectCTheme
import com.hyuse.projectc.ui.components.LocalDrawerState
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Logout

@Composable
fun App() {
    ProjectCTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val coroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val topLevelRoutes = listOf(
            Routes.HOME,
            Routes.EXPENSES_DASHBOARD,
            Routes.UTILITIES,
            Routes.REMINDERS_DASHBOARD,
            Routes.PROFILE
        )

        val shouldShowBottomBar = currentRoute in topLevelRoutes

        CompositionLocalProvider(LocalDrawerState provides drawerState) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    if (shouldShowBottomBar) {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.colorScheme.surface,
                            drawerContentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.width(300.dp)
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "MENU",
                                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                            
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                                label = { Text("DASHBOARD", fontWeight = FontWeight.Bold) },
                                selected = currentRoute == Routes.HOME,
                                onClick = {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Build, contentDescription = null) },
                                label = { Text("UTILITIES", fontWeight = FontWeight.Bold) },
                                selected = currentRoute == Routes.UTILITIES,
                                onClick = {
                                    navController.navigate(Routes.UTILITIES) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                                label = { Text("EXPENSES", fontWeight = FontWeight.Bold) },
                                selected = currentRoute == Routes.EXPENSES_DASHBOARD,
                                onClick = {
                                    navController.navigate(Routes.EXPENSES_DASHBOARD) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                label = { Text("REMINDERS", fontWeight = FontWeight.Bold) },
                                selected = currentRoute == Routes.REMINDERS_DASHBOARD,
                                onClick = {
                                    navController.navigate(Routes.REMINDERS_DASHBOARD) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                label = { Text("PROFILE", fontWeight = FontWeight.Bold) },
                                selected = currentRoute == Routes.PROFILE,
                                onClick = {
                                    navController.navigate(Routes.PROFILE) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                                label = { Text("LOGOUT", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
                                selected = false,
                                onClick = {
                                    // Logout logic is typically handled by auth state observation in NavGraph
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                        NavGraph(navController = navController)

                        if (shouldShowBottomBar) {
                            LucidBottomNavigation(
                                navController = navController,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}
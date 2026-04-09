package com.hyuse.projectc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hyuse.projectc.ui.auth.AuthState
import com.hyuse.projectc.ui.auth.AuthViewModel
import com.hyuse.projectc.ui.auth.LoginScreen
import com.hyuse.projectc.ui.auth.SignUpScreen
import com.hyuse.projectc.ui.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Navigation route constants.
 */
object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val HOME = "home"
}

/**
 * Main navigation graph for the app.
 * Handles auth-aware routing: if user is authenticated, navigates to Home;
 * otherwise shows Login/SignUp.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    // Share the AuthViewModel across all auth-related screens
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.authState.collectAsState()

    // React to auth state changes for navigation
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(Routes.HOME) {
                    // Clear backstack so user can't go back to login after signing in
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                // Only navigate to login if we're not already on an auth screen
                val currentRoute = navController.currentDestination?.route
                if (currentRoute == Routes.HOME || currentRoute == null) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> { /* Loading or Error — handled by individual screens */ }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                authState = authState,
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGN_UP)
                },
                onClearError = {
                    authViewModel.clearError()
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                authState = authState,
                onSignUp = { email, password, confirmPassword ->
                    authViewModel.signUp(email, password, confirmPassword)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onClearError = {
                    authViewModel.clearError()
                }
            )
        }

        composable(Routes.HOME) {
            val user = (authState as? AuthState.Authenticated)?.user
            if (user != null) {
                HomeScreen(
                    user = user,
                    onLogout = {
                        authViewModel.logout()
                    }
                )
            }
        }
    }
}

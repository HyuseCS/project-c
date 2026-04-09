package com.hyuse.projectc.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hyuse.projectc.ui.auth.AuthState
import com.hyuse.projectc.ui.auth.AuthViewModel
import com.hyuse.projectc.ui.auth.LoginScreen
import com.hyuse.projectc.ui.auth.SignUpScreen
import com.hyuse.projectc.ui.home.HomeState
import com.hyuse.projectc.ui.home.HomeViewModel
import com.hyuse.projectc.ui.home.HomeScreen
import com.hyuse.projectc.ui.profile.ProfileScreen
import com.hyuse.projectc.ui.profile.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Navigation route constants.
 */
object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val HOME = "home"
    const val PROFILE = "profile"
}

/**
 * Main navigation graph for the app.
 * Handles auth-aware routing and profile completion checks.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                val currentRoute = navController.currentDestination?.route
                if (currentRoute == Routes.LOGIN || currentRoute == Routes.SIGN_UP || currentRoute == null) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            is AuthState.Unauthenticated -> {
                val currentRoute = navController.currentDestination?.route
                if (currentRoute != Routes.LOGIN && currentRoute != Routes.SIGN_UP) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> {}
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
                val homeViewModel: HomeViewModel = koinViewModel()
                val homeState by homeViewModel.homeState.collectAsState()

                LaunchedEffect(user.uid) {
                    homeViewModel.checkProfile(user.uid)
                }

                LaunchedEffect(homeState) {
                    if (homeState is HomeState.ProfileMissing) {
                        navController.navigate(Routes.PROFILE)
                    }
                }

                if (homeState is HomeState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    HomeScreen(
                        user = user,
                        onLogout = {
                            authViewModel.logout()
                        },
                        onNavigateToProfile = {
                            navController.navigate(Routes.PROFILE)
                        }
                    )
                }
            }
        }

        composable(Routes.PROFILE) {
            val user = (authState as? AuthState.Authenticated)?.user
            if (user != null) {
                val profileViewModel: ProfileViewModel = koinViewModel()
                val profileState by profileViewModel.profileState.collectAsState()

                LaunchedEffect(user.uid) {
                    profileViewModel.loadProfile(user.uid)
                }

                ProfileScreen(
                    user = user,
                    profileState = profileState,
                    onSave = { name, university, course ->
                        profileViewModel.saveProfile(
                            uid = user.uid,
                            name = name,
                            email = user.email,
                            university = university,
                            course = course
                        )
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                    onClearError = {
                        profileViewModel.clearError()
                    },
                    onSaveSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.PROFILE) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

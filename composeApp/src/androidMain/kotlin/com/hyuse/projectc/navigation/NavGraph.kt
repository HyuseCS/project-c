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
import com.hyuse.projectc.ui.utilities.ElectricityBillHistoryScreen
import com.hyuse.projectc.ui.utilities.ElectricityBillScreen
import com.hyuse.projectc.ui.utilities.ElectricityBillViewModel
import com.hyuse.projectc.ui.utilities.UtilitiesHubScreen
import com.hyuse.projectc.ui.utilities.predictor.ElectricityPredictorScreen
import com.hyuse.projectc.ui.utilities.predictor.ElectricityPredictorViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Navigation route constants.
 */
object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val UTILITIES = "utilities"
    const val ELECTRICITY_CALCULATOR = "electricity_calculator"
    const val ELECTRICITY_HISTORY = "electricity_history"
    const val ELECTRICITY_PREDICTOR = "electricity_predictor"
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
                    homeViewModel.loadDashboard(user.uid)
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
                } else if (homeState is HomeState.Success) {
                    HomeScreen(
                        state = homeState as HomeState.Success,
                        onLogout = {
                            authViewModel.logout()
                        },
                        onNavigateToProfile = {
                            navController.navigate(Routes.PROFILE)
                        },
                        onNavigateToUtilities = {
                            navController.navigate(Routes.UTILITIES)
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
                    onSave = { name, nickname, university, course ->
                        profileViewModel.saveProfile(
                            uid = user.uid,
                            name = name,
                            nickname = nickname,
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

        composable(Routes.UTILITIES) {
            UtilitiesHubScreen(
                onNavigateToElectricityCalculator = {
                    navController.navigate(Routes.ELECTRICITY_CALCULATOR)
                },
                onNavigateToElectricityPredictor = {
                    navController.navigate(Routes.ELECTRICITY_PREDICTOR)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ELECTRICITY_CALCULATOR) {
            val user = (authState as? AuthState.Authenticated)?.user
            if (user != null) {
                val viewModel: ElectricityBillViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsState()
                val history by viewModel.history.collectAsState()

                LaunchedEffect(user.uid) {
                    viewModel.loadHistory(user.uid)
                }

                ElectricityBillScreen(
                    uiState = uiState,
                    history = history,
                    lastRate = viewModel.getLastRate(),
                    lastCurrency = viewModel.getLastCurrency(),
                    onCalculate = { month, year, prev, curr, rate, currency ->
                        viewModel.calculate(month, year, prev, curr, rate, currency)
                    },
                    onSave = { forceOverwrite, overwriteId ->
                        viewModel.saveResult(user.uid, forceOverwrite, overwriteId)
                    },
                    onNavigateToHistory = {
                        navController.navigate(Routes.ELECTRICITY_HISTORY)
                    },
                    onResetState = {
                        viewModel.resetState()
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Routes.ELECTRICITY_HISTORY) {
            val user = (authState as? AuthState.Authenticated)?.user
            if (user != null) {
                // Since this screen is right below the calculator in the stack, we can grab the same viewmodel
                // or just rely on the history already fetched, but since compose navigation is decoupled, 
                // we instantiate the viewmodel again. Koin will return the instance or create a new one.
                val viewModel: ElectricityBillViewModel = koinViewModel()
                val history by viewModel.history.collectAsState()

                LaunchedEffect(user.uid) {
                    if (history.isEmpty()) {
                        viewModel.loadHistory(user.uid)
                    }
                }

                ElectricityBillHistoryScreen(
                    history = history,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Routes.ELECTRICITY_PREDICTOR) {
            val user = (authState as? AuthState.Authenticated)?.user
            if (user != null) {
                val viewModel: ElectricityPredictorViewModel = koinViewModel()
                val appliances by viewModel.appliances.collectAsState()
                val summary by viewModel.summary.collectAsState()
                val ratePerKwh by viewModel.ratePerKwh.collectAsState()
                val currencySymbol by viewModel.currencySymbol.collectAsState()

                LaunchedEffect(user.uid) {
                    viewModel.loadPreferences(user.uid)
                }

                ElectricityPredictorScreen(
                    appliances = appliances,
                    summary = summary,
                    ratePerKwh = ratePerKwh,
                    currencySymbol = currencySymbol,
                    onAddAppliance = { name, wattage, hours, days, qty ->
                        viewModel.addAppliance(name, wattage, hours, days, qty)
                    },
                    onRemoveAppliance = { id ->
                        viewModel.removeAppliance(id)
                    },
                    onRateChanged = { rate ->
                        viewModel.updateRate(rate)
                    },
                    onCurrencyChanged = { symbol ->
                        viewModel.updateCurrency(symbol)
                    },
                    currencies = viewModel.currencies,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

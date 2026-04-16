package com.hyuse.projectc.di

import com.hyuse.projectc.ui.auth.AuthViewModel
import com.hyuse.projectc.ui.home.HomeViewModel
import com.hyuse.projectc.ui.profile.ProfileViewModel
import com.hyuse.projectc.ui.utilities.ElectricityBillViewModel
import com.hyuse.projectc.ui.utilities.WaterBillViewModel
import com.hyuse.projectc.ui.utilities.predictor.ElectricityPredictorViewModel
import com.hyuse.projectc.ui.expenses.ExpensesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Android app-level dependencies.
 * Provides ViewModels and Android-specific services.
 */
val appModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ElectricityBillViewModel(get(), get(), get()) }
    viewModel { WaterBillViewModel(get(), get(), get()) }
    viewModel { ElectricityPredictorViewModel(get()) }
    viewModel { ExpensesViewModel(get(), get(), get(), get(), get()) }
}


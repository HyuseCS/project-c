package com.hyuse.projectc.di

import com.hyuse.projectc.ui.auth.AuthViewModel
import com.hyuse.projectc.ui.home.HomeViewModel
import com.hyuse.projectc.ui.profile.ProfileViewModel
import com.hyuse.projectc.ui.utilities.ElectricityBillViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Android app-level dependencies.
 * Provides ViewModels and Android-specific services.
 */
val appModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ElectricityBillViewModel(get(), get(), get()) }
}


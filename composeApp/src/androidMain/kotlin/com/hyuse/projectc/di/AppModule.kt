package com.hyuse.projectc.di

import com.hyuse.projectc.ui.auth.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for Android app-level dependencies.
 * Provides ViewModels and Android-specific services.
 */
val appModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
}

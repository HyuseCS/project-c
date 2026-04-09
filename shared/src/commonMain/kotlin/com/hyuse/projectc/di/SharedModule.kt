package com.hyuse.projectc.di

import com.hyuse.projectc.data.repository.AuthRepositoryImpl
import com.hyuse.projectc.domain.repository.AuthRepository
import com.hyuse.projectc.domain.usecase.GetCurrentUserUseCase
import com.hyuse.projectc.domain.usecase.LoginUseCase
import com.hyuse.projectc.domain.usecase.LogoutUseCase
import com.hyuse.projectc.domain.usecase.SignUpUseCase
import org.koin.dsl.module

/**
 * Koin module for shared (cross-platform) dependencies.
 * Provides repositories and use cases available to all platforms.
 */
val sharedModule = module {
    // Repository — singleton since FirebaseAuth instance should be shared
    single<AuthRepository> { AuthRepositoryImpl() }

    // Use cases — factory (new instance each time, they're lightweight)
    factory { SignUpUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
}

package com.hyuse.projectc.di

import com.hyuse.projectc.data.repository.AuthRepositoryImpl
import com.hyuse.projectc.data.repository.ProfileRepositoryImpl
import com.hyuse.projectc.domain.repository.AuthRepository
import com.hyuse.projectc.domain.repository.ProfileRepository
import com.hyuse.projectc.domain.usecase.*
import org.koin.dsl.module

/**
 * Koin module for shared (cross-platform) dependencies.
 * Provides repositories and use cases available to all platforms.
 */
val sharedModule = module {
    // Repositories — singletons
    single<AuthRepository> { AuthRepositoryImpl() }
    single<ProfileRepository> { ProfileRepositoryImpl() }

    // Use cases — factory
    factory { SignUpUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }

    factory { GetProfileUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { ObserveProfileUseCase(get()) }
}

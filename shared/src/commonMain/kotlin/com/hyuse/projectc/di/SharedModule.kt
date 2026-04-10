package com.hyuse.projectc.di

import com.hyuse.projectc.data.repository.AuthRepositoryImpl
import com.hyuse.projectc.data.repository.CalculatorRepositoryImpl
import com.hyuse.projectc.data.repository.ProfileRepositoryImpl
import com.hyuse.projectc.domain.repository.AuthRepository
import com.hyuse.projectc.domain.repository.CalculatorRepository
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
    single<CalculatorRepository> { CalculatorRepositoryImpl() }

    // Auth use cases — factory
    factory { SignUpUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }

    // Profile use cases — factory
    factory { GetProfileUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { ObserveProfileUseCase(get()) }

    // Calculator use cases — factory
    factory { CalculateElectricityBillUseCase() }
    factory { SaveElectricityBillUseCase(get()) }
    factory { GetElectricityBillHistoryUseCase(get()) }
}

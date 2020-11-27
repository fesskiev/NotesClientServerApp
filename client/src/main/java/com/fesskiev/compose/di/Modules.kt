package com.fesskiev.compose.di

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.remote.provideKtorClient
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.presentation.MainScreenViewModel
import com.fesskiev.compose.presentation.AuthScreenViewModel
import com.fesskiev.compose.ui.utils.FieldValidator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { FieldValidator() }
}

val repositoryModule = module {
    single { RepositoryImpl(get()) as Repository }
}

val viewModelModule = module {
    viewModel { MainScreenViewModel(get()) }
    viewModel { AuthScreenViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { NotesUseCase(get()) }
    factory { RegistrationUseCase(get(), get()) }
    factory { LoginUseCase(get(), get()) }
}

val networkModule = module {
    single { provideKtorClient() }
}

val appModules = listOf(appModule, viewModelModule, useCaseModule, repositoryModule, networkModule)
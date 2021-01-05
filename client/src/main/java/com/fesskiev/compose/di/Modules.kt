package com.fesskiev.compose.di

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.remote.provideKtorClient
import com.fesskiev.compose.domain.*
import com.fesskiev.compose.presentation.*
import com.fesskiev.compose.ui.utils.DataStoreManager
import com.fesskiev.compose.ui.utils.FieldValidator
import com.fesskiev.compose.ui.utils.NetworkManager
import com.fesskiev.compose.ui.utils.ThemeManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NetworkManager(get()) }
    factory { DataStoreManager(get()) }
    factory { ThemeManager() }
    factory { FieldValidator() }
}

val repositoryModule = module {
    single { RepositoryImpl(get()) as Repository }
}

val viewModelModule = module {
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { NotesDetailsViewModel(get()) }
    viewModel { AddNoteViewModel(get()) }
    viewModel { EditNoteViewModel(get(), get()) }
    viewModel { NotesListViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { ThemeModeUseCase(get(), get()) }
    factory { GetNoteByIdUseCase(get()) }
    factory { AddNoteUseCase(get()) }
    factory { NotesListUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }
    factory { EditNoteUseCase(get()) }
    factory { RegistrationUseCase(get(), get()) }
    factory { LoginUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
}

val networkModule = module {
    single { provideKtorClient(get()) }
}

val appModules = listOf(appModule, viewModelModule, useCaseModule, repositoryModule, networkModule)
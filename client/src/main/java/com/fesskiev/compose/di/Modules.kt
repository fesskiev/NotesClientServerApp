package com.fesskiev.compose.di

import com.fesskiev.compose.data.remote.RemoteService
import com.fesskiev.compose.data.remote.RemoteServiceImpl
import com.fesskiev.compose.data.remote.provideKtorClient
import com.fesskiev.compose.domain.*
import com.fesskiev.compose.presentation.AuthViewModel
import com.fesskiev.compose.presentation.NotesViewModel
import com.fesskiev.compose.presentation.SettingsViewModel
import com.fesskiev.compose.ui.utils.DataStoreManager
import com.fesskiev.compose.ui.utils.FieldValidator
import com.fesskiev.compose.ui.utils.NetworkManager
import com.fesskiev.compose.ui.utils.ThemeManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NetworkManager(get()) }
    single { DataStoreManager(get()) }
    factory { ThemeManager() }
    factory { FieldValidator() }
}

val repositoryModule = module {
    single { RemoteServiceImpl(get()) as RemoteService }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { NotesViewModel(get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { ThemeModeUseCase(get(), get()) }
    factory { AddNoteUseCase(get()) }
    factory { GetNotesUseCase(get()) }
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
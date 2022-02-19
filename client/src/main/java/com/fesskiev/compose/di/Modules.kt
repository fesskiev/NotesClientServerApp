package com.fesskiev.compose.di

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.local.DatabaseImpl
import com.fesskiev.compose.data.local.DatabaseSource
import com.fesskiev.compose.data.local.provideSqlDelight
import com.fesskiev.compose.data.remote.NetworkSource
import com.fesskiev.compose.data.remote.NetworkSourceImpl
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

val networkModule = module {
    single { provideKtorClient(get()) }
}

val dataModule = module {
    single { provideSqlDelight(get()) }
    single { DatabaseImpl(get()) as DatabaseSource }
    single { NetworkSourceImpl(get()) as NetworkSource }
    single { RepositoryImpl(get(), get()) as Repository }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { NotesViewModel(get(), get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { ThemeModeUseCase(get(), get()) }
    factory { RefreshUseCase(get()) }
    factory { AddNoteUseCase(get()) }
    factory { PagingNotesUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }
    factory { EditNoteUseCase(get()) }
    factory { RegistrationUseCase(get(), get()) }
    factory { LoginUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
}


val appModules = listOf(appModule, viewModelModule, useCaseModule, dataModule, networkModule)
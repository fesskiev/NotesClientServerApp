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
import com.fesskiev.compose.presentation.AuthPresenter
import com.fesskiev.compose.presentation.NotesPresenter
import com.fesskiev.compose.presentation.SettingsPresenter
import com.fesskiev.compose.state.UiStateSaver
import com.fesskiev.compose.state.provideUiStateSharedPreferences
import com.fesskiev.compose.ui.utils.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { NetworkManager(get()) }
    single(named("uiStatePreferences")) { provideUiStateSharedPreferences(get()) }
    single(named("appPreferences")) { provideAppSharedPreferences(get()) }
    single { UiStateSaver(get(named("uiStatePreferences"))) }
    factory { ThemeManager(get(named("appPreferences"))) }
    factory { FieldValidator() }
}

val networkModule = module {
    single { provideKtorClient(get()) }
}

val dataModule = module {
    single { provideSqlDelight(get()) }
    single<DatabaseSource> { DatabaseImpl(get()) }
    single<NetworkSource> { NetworkSourceImpl(get())}
    single<Repository> { RepositoryImpl(get(), get()) }
}

val presenterModule = module {
    factory { NotesPresenter(get(), get(), get(), get(), get(), get(), get()) }
    factory { AuthPresenter(get(), get()) }
    factory { SettingsPresenter(get(), get()) }
}

val useCaseModule = module {
    factory { ThemeModeUseCase(get()) }
    factory { SearchNotesUseCase(get()) }
    factory { RefreshNotesUseCase(get()) }
    factory { AddNoteUseCase(get()) }
    factory { PagingNotesUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }
    factory { EditNoteUseCase(get()) }
    factory { RegistrationUseCase(get(), get()) }
    factory { LoginUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
}


val appModules = listOf(appModule, presenterModule, useCaseModule, dataModule, networkModule)
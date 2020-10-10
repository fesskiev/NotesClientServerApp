package com.fesskiev.compose.di

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.fesskiev.compose.BuildConfig
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.remote.AppInterceptor
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.presentation.NotesViewModel
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import okhttp3.Interceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { provideRepository(get()) }
}

val viewModelModule = module {
    viewModel { (handle: SavedStateHandle) -> NotesViewModel(handle, get()) }
}

val useCaseModule = module {
    single { provideNotesUseCase(get()) }
}

val networkModule = module {
    single { provideAppInterceptor() }
    single { provideKtorClient(get()) }
}


private fun provideKtorClient(appInterceptor: Interceptor): HttpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Ktor", message)
            }
        }
        level = LogLevel.ALL
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15000L
        connectTimeoutMillis = 15000L
        socketTimeoutMillis = 15000L
    }
    defaultRequest {
        host = BuildConfig.HOST
        port = BuildConfig.PORT
    }
    engine {
        addInterceptor(appInterceptor)
    }
}

private fun provideAppInterceptor(): Interceptor = AppInterceptor()

private fun provideRepository(httpClient: HttpClient): Repository = RepositoryImpl(httpClient)

private fun provideNotesUseCase(repository: Repository): NotesUseCase = NotesUseCase(repository)

val appModules = listOf(viewModelModule, useCaseModule, repositoryModule, networkModule)
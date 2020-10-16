package com.fesskiev.compose.di

import android.util.Log
import com.fesskiev.compose.BuildConfig
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.remote.AppInterceptor
import com.fesskiev.compose.data.remote.UnauthorizedException
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.presentation.NotesViewModel
import com.fesskiev.compose.presentation.RegistrationViewModel
import com.fesskiev.compose.ui.utils.FieldValidator
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import okhttp3.Interceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    factory { provideFieldValidator() }
}

val repositoryModule = module {
    single { provideRepository(get()) }
}

val viewModelModule = module {
    viewModel { NotesViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get()) }
}

val useCaseModule = module {
    factory { NotesUseCase(get()) }
    factory { RegistrationUseCase(get(), get()) }
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
    HttpResponseValidator{
        validateResponse { response: HttpResponse ->
            when (response.status.value) {
                401 -> throw UnauthorizedException()
            }
        }
    }
}

private fun provideAppInterceptor(): Interceptor = AppInterceptor()

private fun provideRepository(httpClient: HttpClient): Repository = RepositoryImpl(httpClient)

private fun provideFieldValidator(): FieldValidator = FieldValidator()

val appModules = listOf(appModule, viewModelModule, useCaseModule, repositoryModule, networkModule)
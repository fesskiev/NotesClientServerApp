package com.fesskiev.compose.di

import androidx.lifecycle.SavedStateHandle
import com.fesskiev.compose.BuildConfig
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.compose.data.remote.ApiService
import com.fesskiev.compose.data.remote.AppInterceptor
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.presentation.NotesViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get(), get()) }
    single { provideMoshi() }
    single { provideMoshiConverterFactory(get()) }
    single { provideRetrofit(get(), get()) }
    single { provideApiService(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BuildConfig.BASE_URL)
    .addConverterFactory(moshiConverterFactory)
    .build()

private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor, appInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(65, TimeUnit.SECONDS)
    .readTimeout(65, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
    .addInterceptor(appInterceptor)
    .addNetworkInterceptor(interceptor)
    .build()

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

private fun provideAppInterceptor(): Interceptor = AppInterceptor()

private fun provideMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

private fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)

private fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

private fun provideNotesUseCase(repository: Repository): NotesUseCase = NotesUseCase(repository)

val appModules = listOf(viewModelModule, useCaseModule, repositoryModule, networkModule)
package com.example.orbitmvi_sample.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.orbitmvi_sample.data.repo.ApiRepositoryImpl
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import com.example.orbitmvi_sample.network.ApiKeyInterceptor
import com.example.orbitmvi_sample.network.ApiService
import com.example.orbitmvi_sample.network.Const
import com.example.orbitmvi_sample.presentation.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideInterceptor(): ApiKeyInterceptor {
    return ApiKeyInterceptor()
}

fun provideHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(apiKeyInterceptor)
        .build()
}

fun provideConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Const.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

val appModule = module {
    single { provideInterceptor() }
    single { provideHttpClient(get()) }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }

    single<ApiRepository> { ApiRepositoryImpl(apiService = get()) }
    viewModel { AppViewModel(scope = get(), repo = get()) }
    single { CoroutineScope(Dispatchers.IO) }
}

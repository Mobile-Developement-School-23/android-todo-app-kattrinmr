package com.katerina.todoapp.di.modules

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.katerina.todoapp.data.api.TasksApiService
import com.katerina.todoapp.data.api.interceptors.CheckConnectionInterceptor
import com.katerina.todoapp.data.utils.BASE_URL
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(CheckConnectionInterceptor(context))
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideTasksApiService(retrofit: Retrofit): TasksApiService =
        retrofit.create(TasksApiService::class.java)
}
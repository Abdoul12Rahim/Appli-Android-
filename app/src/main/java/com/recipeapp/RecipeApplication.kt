package com.recipeapp

import android.app.Application
import com.recipeapp.data.local.AppDatabase
import com.recipeapp.data.remote.MealApiService
import com.recipeapp.data.repository.MealRepositoryImpl
import com.recipeapp.domain.repository.MealRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RecipeApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    val apiService: MealApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }

    val repository: MealRepository by lazy {
        MealRepositoryImpl(apiService, database.mealDao(), database.categoryDao())
    }
}

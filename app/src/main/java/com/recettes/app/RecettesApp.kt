package com.recettes.app

import android.app.Application
import com.recettes.app.data.db.AppDatabase
import com.recettes.app.data.api.RetrofitClient
import com.recettes.app.data.repository.MealRepository

class RecettesApp : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy {
        MealRepository(
            api = RetrofitClient.mealApiService,
            dao = database.mealDao()
        )
    }
}

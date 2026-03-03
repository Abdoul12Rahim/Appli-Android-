package com.recipeapp.domain.repository

import com.recipeapp.domain.model.Category
import com.recipeapp.domain.model.Meal

interface MealRepository {
    suspend fun searchMeals(query: String): Result<List<Meal>>
    suspend fun getMealById(id: String): Result<Meal>
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getMealsByCategory(category: String): Result<List<Meal>>
}

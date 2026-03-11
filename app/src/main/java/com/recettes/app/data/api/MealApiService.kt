package com.recettes.app.data.api

import com.recettes.app.data.model.CategoryResponse
import com.recettes.app.data.model.MealDetailResponse
import com.recettes.app.data.model.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    @GET("lookup.php")
    suspend fun getMealDetail(@Query("i") mealId: String): MealDetailResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealListResponse
}

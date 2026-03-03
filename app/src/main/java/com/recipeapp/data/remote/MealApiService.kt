package com.recipeapp.data.remote

import com.recipeapp.data.remote.dto.CategoriesResponse
import com.recipeapp.data.remote.dto.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") s: String): MealsResponse

    @GET("lookup.php")
    suspend fun lookupMeal(@Query("i") i: String): MealsResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") c: String): MealsResponse
}

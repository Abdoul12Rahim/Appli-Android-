package com.recettes.app.data.db

import androidx.room.*

@Dao
interface MealDao {

    // ---------- Meal list caching ----------

    @Query("SELECT * FROM meals WHERE category = :category ORDER BY name ASC")
    suspend fun getMealsByCategory(category: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchMeals(query: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE id = :id LIMIT 1")
    suspend fun getMealById(id: String): MealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE category = :category")
    suspend fun deleteMealsByCategory(category: String)

    @Query("SELECT COUNT(*) FROM meals WHERE category = :category")
    suspend fun countByCategory(category: String): Int
}

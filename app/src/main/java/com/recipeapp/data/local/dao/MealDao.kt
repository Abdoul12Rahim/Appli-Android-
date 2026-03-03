package com.recipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipeapp.data.local.entity.MealEntity

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE strCategory = :category")
    suspend fun getMealsByCategory(category: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE strMeal LIKE '%' || :query || '%'")
    suspend fun searchMeals(query: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE idMeal = :id")
    suspend fun getMealById(id: String): MealEntity?

    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE timestamp < :timestamp")
    suspend fun deleteOldMeals(timestamp: Long)
}

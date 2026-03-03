package com.recipeapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.recipeapp.data.local.dao.CategoryDao
import com.recipeapp.data.local.dao.MealDao
import com.recipeapp.data.local.entity.CategoryEntity
import com.recipeapp.data.local.entity.MealEntity
import com.recipeapp.data.remote.MealApiService
import com.recipeapp.data.remote.dto.MealDto
import com.recipeapp.domain.model.Category
import com.recipeapp.domain.model.Meal
import com.recipeapp.domain.repository.MealRepository

class MealRepositoryImpl(
    private val apiService: MealApiService,
    private val mealDao: MealDao,
    private val categoryDao: CategoryDao
) : MealRepository {

    private val gson = Gson()
    private val cacheMaxAge = 60 * 60 * 1000L // 1 hour in ms

    override suspend fun searchMeals(query: String): Result<List<Meal>> {
        return try {
            val response = apiService.searchMeals(query)
            val meals = response.meals ?: emptyList()
            val entities = meals.map { it.toEntity() }
            mealDao.insertMeals(entities)
            val oldTimestamp = System.currentTimeMillis() - cacheMaxAge
            mealDao.deleteOldMeals(oldTimestamp)
            Result.success(entities.map { it.toDomain(gson) })
        } catch (e: Exception) {
            val cached = if (query.isBlank()) mealDao.getAllMeals() else mealDao.searchMeals(query)
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain(gson) })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMealById(id: String): Result<Meal> {
        return try {
            val response = apiService.lookupMeal(id)
            val meal = response.meals?.firstOrNull() ?: return Result.failure(Exception("Meal not found"))
            val entity = meal.toEntity()
            mealDao.insertMeal(entity)
            Result.success(entity.toDomain(gson))
        } catch (e: Exception) {
            val cached = mealDao.getMealById(id)
            if (cached != null) {
                Result.success(cached.toDomain(gson))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getCategories()
            val categories = response.categories ?: emptyList()
            val entities = categories.map {
                CategoryEntity(
                    idCategory = it.idCategory,
                    strCategory = it.strCategory,
                    strCategoryThumb = it.strCategoryThumb
                )
            }
            categoryDao.insertCategories(entities)
            Result.success(entities.map { Category(it.idCategory, it.strCategory, it.strCategoryThumb) })
        } catch (e: Exception) {
            val cached = categoryDao.getAllCategories()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { Category(it.idCategory, it.strCategory, it.strCategoryThumb) })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMealsByCategory(category: String): Result<List<Meal>> {
        return try {
            val response = apiService.filterByCategory(category)
            val dtos = response.meals ?: emptyList()
            val entities = dtos.map { dto ->
                MealEntity(
                    idMeal = dto.idMeal ?: "",
                    strMeal = dto.strMeal ?: "",
                    strCategory = category,
                    strArea = "",
                    strInstructions = "",
                    strMealThumb = dto.strMealThumb ?: "",
                    strTags = "",
                    strYoutube = "",
                    ingredientsJson = "[]"
                )
            }.filter { it.idMeal.isNotBlank() }
            mealDao.insertMeals(entities)
            Result.success(entities.map { it.toDomain(gson) })
        } catch (e: Exception) {
            val cached = mealDao.getMealsByCategory(category)
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain(gson) })
            } else {
                Result.failure(e)
            }
        }
    }

    private fun MealDto.toEntity(): MealEntity {
        val ingredients = getIngredients()
        val ingredientsJson = gson.toJson(ingredients.map { listOf(it.first, it.second) })
        return MealEntity(
            idMeal = idMeal ?: "",
            strMeal = strMeal ?: "",
            strCategory = strCategory ?: "",
            strArea = strArea ?: "",
            strInstructions = strInstructions ?: "",
            strMealThumb = strMealThumb ?: "",
            strTags = strTags ?: "",
            strYoutube = strYoutube ?: "",
            ingredientsJson = ingredientsJson
        )
    }

    private fun MealEntity.toDomain(gson: Gson): Meal {
        val type = object : TypeToken<List<List<String>>>() {}.type
        val rawList: List<List<String>> = try {
            gson.fromJson(ingredientsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        val ingredients = rawList.mapNotNull { pair ->
            if (pair.size >= 2) pair[0] to pair[1] else null
        }
        return Meal(
            idMeal = idMeal,
            strMeal = strMeal,
            strCategory = strCategory,
            strArea = strArea,
            strInstructions = strInstructions,
            strMealThumb = strMealThumb,
            strTags = strTags,
            strYoutube = strYoutube,
            ingredients = ingredients
        )
    }
}

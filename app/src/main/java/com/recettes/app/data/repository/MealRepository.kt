package com.recettes.app.data.repository

import com.recettes.app.data.api.MealApiService
import com.recettes.app.data.db.MealDao
import com.recettes.app.data.db.MealEntity
import com.recettes.app.data.model.Category
import com.recettes.app.data.model.Meal
import com.recettes.app.data.model.MealDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/** Simple DTO for caching ingredient+measure pairs as JSON. */
private data class IngredientEntry(val ingredient: String, val measure: String)

class MealRepository(
    private val api: MealApiService,
    private val dao: MealDao
) {
    private val gson = Gson()
    private val ingListType = object : TypeToken<List<IngredientEntry>>() {}.type

    // ---------- Categories ----------

    suspend fun getCategories(): List<Category> = api.getCategories().categories

    // ---------- Meals by category ----------

    suspend fun getMealsByCategory(category: String, isOnline: Boolean): List<Meal> {
        return if (isOnline) {
            val remote = api.getMealsByCategory(category).meals ?: emptyList()
            val entities = remote.map { meal ->
                MealEntity(
                    id = meal.id,
                    name = meal.name,
                    thumbnail = meal.thumbnail,
                    category = category,
                    area = meal.area ?: "",
                    instructions = "",
                    ingredientsJson = "[]",
                    tags = ""
                )
            }
            dao.deleteMealsByCategory(category)
            dao.insertMeals(entities)
            remote
        } else {
            dao.getMealsByCategory(category).map { it.toMeal() }
        }
    }

    // ---------- Search ----------

    suspend fun searchMeals(query: String, isOnline: Boolean): List<Meal> {
        return if (isOnline) {
            api.searchMeals(query).meals ?: emptyList()
        } else {
            dao.searchMeals(query).map { it.toMeal() }
        }
    }

    // ---------- Detail ----------

    suspend fun getMealDetail(id: String, isOnline: Boolean): MealDetail? {
        return if (isOnline) {
            val detail = api.getMealDetail(id).meals?.firstOrNull()
            detail?.let { cacheDetail(it) }
            detail
        } else {
            dao.getMealById(id)?.toMealDetail()
        }
    }

    private suspend fun cacheDetail(detail: MealDetail) {
        val entries = detail.getIngredientList()
            .map { (ing, meas) -> IngredientEntry(ingredient = ing, measure = meas) }
        val entity = MealEntity(
            id = detail.id,
            name = detail.name,
            thumbnail = detail.thumbnail ?: "",
            category = detail.category ?: "",
            area = detail.area ?: "",
            instructions = detail.instructions ?: "",
            ingredientsJson = gson.toJson(entries),
            tags = detail.tags ?: ""
        )
        dao.insertMeal(entity)
    }

    // ---------- Mappers ----------

    private fun MealEntity.toMeal() = Meal(
        id = id,
        name = name,
        thumbnail = thumbnail,
        category = category,
        area = area
    )

    private fun MealEntity.toMealDetail(): MealDetail {
        val entries: List<IngredientEntry> =
            gson.fromJson(ingredientsJson, ingListType) ?: emptyList()

        // Distribute ingredient/measure pairs across the 20 fixed slots
        fun ing(i: Int) = entries.getOrNull(i)?.ingredient
        fun meas(i: Int) = entries.getOrNull(i)?.measure

        return MealDetail(
            id = id, name = name, category = category, area = area,
            instructions = instructions, thumbnail = thumbnail, tags = tags, youtube = null,
            ingredient1  = ing(0),  ingredient2  = ing(1),  ingredient3  = ing(2),
            ingredient4  = ing(3),  ingredient5  = ing(4),  ingredient6  = ing(5),
            ingredient7  = ing(6),  ingredient8  = ing(7),  ingredient9  = ing(8),
            ingredient10 = ing(9),  ingredient11 = ing(10), ingredient12 = ing(11),
            ingredient13 = ing(12), ingredient14 = ing(13), ingredient15 = ing(14),
            ingredient16 = ing(15), ingredient17 = ing(16), ingredient18 = ing(17),
            ingredient19 = ing(18), ingredient20 = ing(19),
            measure1  = meas(0),  measure2  = meas(1),  measure3  = meas(2),
            measure4  = meas(3),  measure5  = meas(4),  measure6  = meas(5),
            measure7  = meas(6),  measure8  = meas(7),  measure9  = meas(8),
            measure10 = meas(9),  measure11 = meas(10), measure12 = meas(11),
            measure13 = meas(12), measure14 = meas(13), measure15 = meas(14),
            measure16 = meas(15), measure17 = meas(16), measure18 = meas(17),
            measure19 = meas(18), measure20 = meas(19)
        )
    }
}


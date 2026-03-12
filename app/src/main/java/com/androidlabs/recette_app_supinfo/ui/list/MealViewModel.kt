package com.androidlabs.recette_app_supinfo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch

// --- 1. LES MODÈLES DE DONNÉES (DOIVENT ÊTRE EN DEHORS DE LA CLASSE) ---

@Serializable
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String? = null,
    val strIngredient1: String? = null, val strMeasure1: String? = null,
    val strIngredient2: String? = null, val strMeasure2: String? = null,
    val strIngredient3: String? = null, val strMeasure3: String? = null,
    val strIngredient4: String? = null, val strMeasure4: String? = null,
    val strIngredient5: String? = null, val strMeasure5: String? = null,
    val strIngredient6: String? = null, val strMeasure6: String? = null,
    val strIngredient7: String? = null, val strMeasure7: String? = null,
    val strIngredient8: String? = null, val strMeasure8: String? = null,
    val strIngredient9: String? = null, val strMeasure9: String? = null,
    val strIngredient10: String? = null, val strMeasure10: String? = null
)

@Serializable
data class MealResponse(val meals: List<Meal>?)

@Serializable
data class CategoryItem(val strCategory: String)

@Serializable
data class CategoryResponse(val meals: List<CategoryItem>?)

// --- 2. LE VIEWMODEL ---

class MealViewModel : ViewModel() {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    private val _filteredMeals = MutableStateFlow<List<Meal>>(emptyList())
    val filteredMeals: StateFlow<List<Meal>> = _filteredMeals

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedMeal = MutableStateFlow<Meal?>(null)
    val selectedMeal: StateFlow<Meal?> = _selectedMeal

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        searchMeals("chicken")
        fetchCategories()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length > 2) searchMeals(query)
    }

    fun searchMeals(query: String = "chicken") { // On met une valeur par défaut
        viewModelScope.launch {
            try {
                val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
                val response: MealResponse = client.get(url).body()
                _filteredMeals.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredMeals.value = emptyList() // Sécurité
            }
        }
    }

    fun getMealById(id: String) {
        viewModelScope.launch {
            try {
                val response: MealResponse = client.get("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id").body()
                _selectedMeal.value = response.meals?.firstOrNull()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response: CategoryResponse = client.get("https://www.themealdb.com/api/json/v1/1/list.php?c=list").body()
                _categories.value = response.meals?.map { it.strCategory } ?: emptyList()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response: MealResponse = client.get("https://www.themealdb.com/api/json/v1/1/filter.php?c=$category").body()
                _filteredMeals.value = response.meals ?: emptyList()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}
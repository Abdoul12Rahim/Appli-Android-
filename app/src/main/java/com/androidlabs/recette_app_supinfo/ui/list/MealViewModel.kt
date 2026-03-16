package com.androidlabs.recette_app_supinfo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.androidlabs.recette_app_supinfo.data.RecipeDao
import com.androidlabs.recette_app_supinfo.data.RecipeEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch

// --- 1. MODÈLES DE DONNÉES ---

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
    val strIngredient5: String? = null, val strMeasure5: String? = null
)

@Serializable
data class MealResponse(val meals: List<Meal>?)

@Serializable
data class CategoryItem(val strCategory: String)

@Serializable
data class CategoryResponse(val meals: List<CategoryItem>?)

// --- 2. VIEWMODEL ---

class MealViewModel(private val recipeDao: RecipeDao) : ViewModel() {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
        }
    }

    val mealsFromDb: StateFlow<List<RecipeEntity>> = recipeDao.getAllRecipes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun searchMeals(query: String = "chicken") {
        viewModelScope.launch {
            try {
                val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
                val response: MealResponse = client.get(url).body()
                val apiMeals = response.meals ?: emptyList()
                _filteredMeals.value = apiMeals

                // SAUVEGARDE ROOM : Correction des erreurs ligne 104
                if (apiMeals.isNotEmpty()) {
                    val entities = apiMeals.map { meal ->
                        RecipeEntity(
                            idMeal = meal.idMeal,
                            strMeal = meal.strMeal,
                            strMealThumb = meal.strMealThumb,
                            strInstructions = meal.strInstructions,
                            strIngredients = meal.strIngredient1
                        )
                    }
                    recipeDao.insertRecipes(entities)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun getMealById(id: String) {
        viewModelScope.launch {
            try {
                val response: MealResponse = client.get("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id").body()
                val meal = response.meals?.firstOrNull()
                _selectedMeal.value = meal

                meal?.let { m ->
                    recipeDao.insertRecipes(listOf(
                        RecipeEntity(m.idMeal, m.strMeal, m.strMealThumb, m.strInstructions, m.strIngredient1)
                    ))
                }
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

// --- 3. FACTORY ---

class MealViewModelFactory(private val recipeDao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
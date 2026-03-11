package com.recettes.app.ui.recipelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recettes.app.RecettesApp
import com.recettes.app.data.model.Category
import com.recettes.app.data.model.Meal
import com.recettes.app.util.NetworkUtil
import com.recettes.app.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as RecettesApp).repository

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _mealsState = MutableLiveData<UiState<List<Meal>>>()
    val mealsState: LiveData<UiState<List<Meal>>> = _mealsState

    private val _searchState = MutableLiveData<UiState<List<Meal>>>()
    val searchState: LiveData<UiState<List<Meal>>> = _searchState

    private val _isOffline = MutableLiveData<Boolean>()
    val isOffline: LiveData<Boolean> = _isOffline

    private var selectedCategory: String = DEFAULT_CATEGORY
    private var searchJob: Job? = null

    companion object {
        private const val DEFAULT_CATEGORY = "Beef"
        private const val SEARCH_DEBOUNCE_MS = 400L
    }

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val cats = repository.getCategories()
                _categories.value = cats
                // Auto-select first category after loading
                if (cats.isNotEmpty()) {
                    loadMeals(cats.first().name)
                }
            } catch (e: Exception) {
                // If categories fail to load, use default
                loadMeals(selectedCategory)
            }
        }
    }

    fun loadMeals(category: String) {
        selectedCategory = category
        _mealsState.value = UiState.Loading
        viewModelScope.launch {
            val isOnline = NetworkUtil.isOnline(getApplication())
            _isOffline.value = !isOnline
            try {
                val meals = repository.getMealsByCategory(category, isOnline)
                _mealsState.value = if (meals.isEmpty()) UiState.Empty else UiState.Success(meals)
            } catch (e: Exception) {
                _mealsState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun search(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchState.value = UiState.Empty
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            _searchState.value = UiState.Loading
            val isOnline = NetworkUtil.isOnline(getApplication())
            try {
                val meals = repository.searchMeals(query.trim(), isOnline)
                _searchState.value = if (meals.isEmpty()) UiState.Empty else UiState.Success(meals)
            } catch (e: Exception) {
                _searchState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun refresh() = loadMeals(selectedCategory)
}

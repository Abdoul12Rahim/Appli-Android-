package com.recipeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.recipeapp.domain.model.Category
import com.recipeapp.domain.model.Meal
import com.recipeapp.domain.repository.MealRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class MainUiState(
    val meals: List<Meal> = emptyList(),
    val displayedMeals: List<Meal> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "",
    val currentPage: Int = 0,
    val hasMorePages: Boolean = false,
    val isLoadingMore: Boolean = false
)

private const val PAGE_SIZE = 30

@OptIn(FlowPreview::class)
class MainViewModel(private val repository: MealRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _searchQueryFlow = MutableStateFlow("")

    init {
        loadCategories()
        loadInitialMeals()

        _searchQueryFlow
            .debounce(400)
            .distinctUntilChanged()
            .onEach { query ->
                fetchMeals(query, _uiState.value.selectedCategory)
            }
            .launchIn(viewModelScope)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().onSuccess { cats ->
                _uiState.value = _uiState.value.copy(categories = cats)
            }
        }
    }

    private fun loadInitialMeals() {
        fetchMeals("a", "")
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        _searchQueryFlow.value = query
    }

    fun selectCategory(category: String) {
        val newCategory = if (_uiState.value.selectedCategory == category) "" else category
        _uiState.value = _uiState.value.copy(selectedCategory = newCategory, searchQuery = "")
        _searchQueryFlow.value = ""
        fetchMeals(_uiState.value.searchQuery, newCategory)
    }

    fun retry() {
        fetchMeals(_uiState.value.searchQuery, _uiState.value.selectedCategory)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMorePages) return
        val nextPage = state.currentPage + 1
        val nextItems = state.meals.take((nextPage + 1) * PAGE_SIZE)
        val hasMore = state.meals.size > nextItems.size
        _uiState.value = state.copy(
            displayedMeals = nextItems,
            currentPage = nextPage,
            hasMorePages = hasMore
        )
    }

    private fun fetchMeals(query: String, category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = when {
                category.isNotBlank() -> repository.getMealsByCategory(category)
                else -> repository.searchMeals(query.ifBlank { "a" })
            }
            result
                .onSuccess { meals ->
                    val displayed = meals.take(PAGE_SIZE)
                    _uiState.value = _uiState.value.copy(
                        meals = meals,
                        displayedMeals = displayed,
                        isLoading = false,
                        error = null,
                        currentPage = 0,
                        hasMorePages = meals.size > PAGE_SIZE
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "An unknown error occurred"
                    )
                }
        }
    }
}

class MainViewModelFactory(private val repository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

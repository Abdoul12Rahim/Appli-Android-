package com.recipeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.recipeapp.domain.model.Meal
import com.recipeapp.domain.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val meal: Meal? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel(private val repository: MealRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMeal(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            repository.getMealById(id)
                .onSuccess { meal ->
                    _uiState.value = DetailUiState(meal = meal)
                }
                .onFailure { e ->
                    _uiState.value = DetailUiState(error = e.message ?: "Failed to load meal")
                }
        }
    }
}

class DetailViewModelFactory(private val repository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

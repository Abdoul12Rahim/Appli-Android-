package com.recettes.app.ui.recipedetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recettes.app.RecettesApp
import com.recettes.app.data.model.MealDetail
import com.recettes.app.util.NetworkUtil
import com.recettes.app.util.UiState
import kotlinx.coroutines.launch

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as RecettesApp).repository

    private val _detailState = MutableLiveData<UiState<MealDetail>>()
    val detailState: LiveData<UiState<MealDetail>> = _detailState

    fun loadDetail(mealId: String) {
        if (_detailState.value is UiState.Success) return // already loaded
        _detailState.value = UiState.Loading
        viewModelScope.launch {
            val isOnline = NetworkUtil.isOnline(getApplication())
            try {
                val detail = repository.getMealDetail(mealId, isOnline)
                _detailState.value = if (detail == null) UiState.Empty else UiState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }
}

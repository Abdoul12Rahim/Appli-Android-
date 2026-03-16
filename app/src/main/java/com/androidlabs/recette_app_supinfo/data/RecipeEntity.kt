package com.androidlabs.recette_app_supinfo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    // --- LES DEUX LIGNES À AJOUTER SONT ICI ---
    val strInstructions: String?,
    val strIngredients: String?
)
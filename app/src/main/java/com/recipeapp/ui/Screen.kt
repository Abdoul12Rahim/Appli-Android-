package com.recipeapp.ui

sealed class Screen {
    object Splash : Screen()
    object Main : Screen()
    data class Detail(val mealId: String) : Screen()
}

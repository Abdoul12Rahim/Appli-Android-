package com.androidlabs.recette_app_supinfo.ui.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.androidlabs.recette_app_supinfo.data.AppDatabase
import kotlinx.coroutines.delay
import com.androidlabs.recette_app_supinfo.ui.detail.RecipeDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. On initialise la base de données (AJOUTE ÇA)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recipe-db"
        ).build()
        val recipeDao = db.recipeDao()

        setContent {
            val navController = rememberNavController()

            // 2. On crée le ViewModel avec la Factory pour lui donner le recipeDao
            val sharedViewModel: MealViewModel = viewModel(
                factory = MealViewModelFactory(recipeDao)
            )

            // On garde ton NavHost exactement comme il était
            NavHost(navController = navController, startDestination = "meal_list") {
                composable("meal_list") {
                    RecipeListScreen(
                        mealViewModel = sharedViewModel,
                        onNavigateToDetail = { id -> navController.navigate("meal_detail/$id") },
                        onNavigateToHome = { }
                    )
                }
                composable("meal_detail/{mealId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("mealId")
                    RecipeDetailScreen(
                        recipeId = id,
                        viewModel = sharedViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }

    @Composable
    fun SplashScreenOrange() {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF13EC13)),
            contentAlignment = Alignment.Center
        ) {
            Text("Dabali Express", color = Color.White, fontSize = 32.sp)
        }
    }
}
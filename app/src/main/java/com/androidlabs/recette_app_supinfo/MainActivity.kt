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
import kotlinx.coroutines.delay
import com.androidlabs.recette_app_supinfo.ui.detail.RecipeDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Dans MainActivity.kt, à l'intérieur de setContent { ... }
            val navController = rememberNavController()

// DÉCLARE-LE ICI (ne change rien d'autre)
            val sharedViewModel: MealViewModel = viewModel()

// Ensuite, dans ton NavHost, utilise "sharedViewModel" au lieu de "mealViewModel"
            NavHost(navController = navController, startDestination = "meal_list") {
                composable("meal_list") {
                    RecipeListScreen(
                        mealViewModel = sharedViewModel, // <--- TRÈS IMPORTANT
                        onNavigateToDetail = { id -> navController.navigate("meal_detail/$id") },
                        onNavigateToHome = { }
                    )
                }
                composable("meal_detail/{mealId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("mealId")
                    RecipeDetailScreen(
                        recipeId = id,
                        viewModel = sharedViewModel, // <--- LE MÊME ICI AUSSI
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreenOrange() {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF13EC13)), contentAlignment = Alignment.Center) {
        Text("Dabali Express", color = Color.White, fontSize = 32.sp)
    }
}
package com.androidlabs.recette_app_supinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Tes 3 écrans importés :
import com.androidlabs.recette_app_supinfo.ui.Home.HomeScreen
import com.androidlabs.recette_app_supinfo.ui.list.RecipeListScreen
import com.androidlabs.recette_app_supinfo.ui.detail.RecipeDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Utilise le thème par défaut de Material3
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // On dit que l'application démarre sur "home"
                    NavHost(navController = navController, startDestination = "home") {

                        // 1. Écran d'Accueil
                        composable("home") {
                            HomeScreen(
                                onNavigateToList = {
                                    navController.navigate("list") {
                                        // On empêche l'utilisateur de revenir sur l'écran de chargement avec la touche retour
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Écran Liste
                        composable("list") {
                            RecipeListScreen(
                                onNavigateToDetail = { recipeId ->
                                    navController.navigate("detail/$recipeId")
                                }
                            )
                        }

                        // 3. Écran Détail
                        composable("detail/{recipeId}") { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId")
                            RecipeDetailScreen(
                                recipeId = recipeId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.androidlabs.recette_app_supinfo.ui.detail
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RecipeDetailScreen(recipeId: String?, onNavigateBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Détails de la recette ID : $recipeId", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onNavigateBack) {
                Text("Retour à la liste")
            }
        }
    }
}
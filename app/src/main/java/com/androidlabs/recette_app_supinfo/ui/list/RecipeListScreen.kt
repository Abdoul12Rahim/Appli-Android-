package com.androidlabs.recette_app_supinfo.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RecipeListScreen(onNavigateToDetail: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Liste des Recettes & Recherche", style = MaterialTheme.typography.headlineMedium)
            // Ce bouton simule le clic sur une recette spécifique (ex: ID "52772")
            Button(onClick = { onNavigateToDetail("52772") }) {
                Text("Voir la recette : Poulet Teriyaki")
            }
        }
    }
}
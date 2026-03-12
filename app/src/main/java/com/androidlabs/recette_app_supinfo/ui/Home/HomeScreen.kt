package com.androidlabs.recette_app_supinfo.ui.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Les mêmes couleurs que sur l'écran Liste
val PrimaryGreen = Color(0xFF13EC13)
val BackgroundLight = Color(0xFFF6F8F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToList: () -> Unit) {
    // Le Scaffold nous permet de caler facilement la barre en bas
    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            HomeBottomNavigation(onListClick = onNavigateToList)
        }
    ) { paddingValues ->
        // Le contenu principal de la page d'accueil (centré)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Tu pourras remplacer ça par un joli logo plus tard !
                Text(
                    text = "Project Supinfo LIlle ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Trouvez la recette parfaite aujourd'hui",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// --- LA BARRE DE NAVIGATION (Version Home) ---
@Composable
fun HomeBottomNavigation(onListClick: () -> Unit) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bouton Home (Actif, Vert et Surélevé)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = (-12).dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("HOME", fontSize = 10.sp, color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }

            // Bouton Recipes (Inactif, Gris et Cliquable)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onListClick() } // C'est ici qu'on passe à la liste !
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.List, contentDescription = "Recipes", tint = Color.Gray)
                Text("RECIPES", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        }
    }
}
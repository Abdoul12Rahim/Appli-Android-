package com.androidlabs.recette_app_supinfo.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

val PrimaryGreen = Color(0xFF13EC13)
val BackgroundLight = Color(0xFFF6F8F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(onNavigateToDetail: (String) -> Unit,onNavigateToHome: () -> Unit) {
    // État pour sauvegarder le texte de la barre de recherche
    var searchQuery by remember { mutableStateOf("") }

    // Le Scaffold structure la page avec une barre en haut, le contenu, et une barre en bas
    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            TopHeaderWithSearch(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        },
        bottomBar = {
            CustomBottomNavigation(onHomeClick = onNavigateToHome)
        }
    ) { paddingValues ->
        // LazyColumn est l'équivalent d'un RecyclerView (une liste défilante optimisée)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Respecte l'espace pris par la topBar et bottomBar
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Titre de section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Trending Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { /* Action View All */ }) {
                        Text("View all", color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // --- Fausses cartes pour le design ---
            item {
                RecipeCard(
                    title = "Honey Teriyaki Chicken",
                    imageUrl = "https://www.themealdb.com/images/media/meals/sypxpx1515365095.jpg",
                    onClick = { onNavigateToDetail("52772") } // Faux ID pour tester la navigation
                )
            }
            item {
                RecipeCard(
                    title = "Classic Margherita Pizza",
                    imageUrl = "https://www.themealdb.com/images/media/meals/x0lk931587671540.jpg",
                    onClick = { onNavigateToDetail("53014") }
                )
            }
            item {
                RecipeCard(
                    title = "Rainbow Mediterranean Bowl",
                    imageUrl = "https://www.themealdb.com/images/media/meals/wxyvqq1511723401.jpg",
                    onClick = { onNavigateToDetail("52844") }
                )
            }

            // Espace à la fin pour ne pas être collé à la barre du bas
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// --- COMPOSANTS REUTILISABLES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeaderWithSearch(query: String, onQueryChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        // Logo et Titre
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryGreen.copy(alpha = 0.2f))
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Culinaria", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Barre de recherche
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search recipes (e.g. Teriyaki Chicken)", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            // --- LA CORRECTION EST ICI ---
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryGreen
            ),
            singleLine = true
        )
    }
}

@Composable
fun RecipeCard(title: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CustomBottomNavigation(onHomeClick: () -> Unit) {
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
            // Bouton Home : ON LE REND CLIQUABLE ICI
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onHomeClick() } // L'action est bien reconnue ici !
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Gray)
                Text("HOME", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }

            // Bouton central "Recipes"
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
                    Icon(Icons.Default.List, contentDescription = "Recipes", tint = Color.White)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("RECIPES", fontSize = 10.sp, color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
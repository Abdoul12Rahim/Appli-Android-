package com.androidlabs.recette_app_supinfo.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

// Couleurs de l'app
val PrimaryGreen = Color(0xFF13EC13)
val BackgroundLight = Color(0xFFF6F8F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    mealViewModel: MealViewModel
) {
    val searchQuery by mealViewModel.searchQuery.collectAsState()
    val meals by mealViewModel.filteredMeals.collectAsState()
    val categories by mealViewModel.categories.collectAsState()

    // CORRECTION: On déclare listState ici
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = Color(0xFFF6F8F6),
        topBar = { TopHeaderWithSearch(searchQuery) { mealViewModel.onSearchQueryChange(it) } },
        bottomBar = { CustomBottomNavigation(onNavigateToHome) }
    ) { paddingValues ->
        if (meals.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF13EC13))
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            AssistChip(
                                onClick = { mealViewModel.filterByCategory(category) },
                                label = { Text(category) }
                            )
                        }
                    }
                }
                items(meals) { meal ->
                    RecipeCard(meal.strMeal, meal.strMealThumb) { onNavigateToDetail(meal.idMeal) }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(title: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(180.dp))
            Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeaderWithSearch(query: String, onQueryChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF6F8F6)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(Icons.Default.Menu, contentDescription = null, tint = Color(0xFF13EC13))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Dabali Express", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        OutlinedTextField(
            value = query, onValueChange = onQueryChange,
            placeholder = { Text("Rechercher...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Composable
fun CustomBottomNavigation(onHomeClick: () -> Unit) {
    BottomAppBar(containerColor = Color.White) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onHomeClick) { Icon(Icons.Default.Home, contentDescription = null) }
            IconButton(onClick = { }) { Icon(Icons.Default.List, contentDescription = null, tint = Color(0xFF13EC13)) }
        }
    }
}
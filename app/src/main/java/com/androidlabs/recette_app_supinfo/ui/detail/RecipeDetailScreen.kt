package com.androidlabs.recette_app_supinfo.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.androidlabs.recette_app_supinfo.ui.list.Meal
import com.androidlabs.recette_app_supinfo.ui.list.MealViewModel

val PrimaryGreen = Color(0xFF13EC13)
val BackgroundLight = Color(0xFFF6F8F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String?,
    onNavigateBack: () -> Unit,
    viewModel: MealViewModel
) {
    val selectedMeal by viewModel.selectedMeal.collectAsState()
    val offlineMeals by viewModel.mealsFromDb.collectAsState()
    val mealFromDb = offlineMeals.find { it.idMeal == recipeId }
    val currentMeal = selectedMeal ?: mealFromDb?.let { entity ->

        Meal(
            idMeal = entity.idMeal,
            strMeal = entity.strMeal,
            strMealThumb = entity.strMealThumb,
            strInstructions = entity.strInstructions,
            strIngredient1 = entity.strIngredients // On affiche la chaîne combinée ici
        )
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(recipeId) {
        recipeId?.let { viewModel.getMealById(it) }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                IconButton(
                    onClick = { onNavigateBack() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = PrimaryGreen)
                }
                Text("Détails", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.align(Alignment.Center))
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        currentMeal?.let { meal ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp).clip(RoundedCornerShape(16.dp))) {
                    AsyncImage(
                        model = meal.strMealThumb,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(text = meal.strMeal, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))

                val ingredients = getIngredientsList(meal)
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ingredients (${ingredients.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ingredients.forEach { (name, measure) ->
                        IngredientItem(name = name, description = measure)
                    }
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Instructions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = meal.strInstructions ?: "Aucune instruction disponible hors ligne.", color = Color.Gray, lineHeight = 20.sp)
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    }
}

@Composable
fun IngredientItem(name: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$name ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        if (description.isNotEmpty()) Text(text = "- $description", color = Color.Gray, fontSize = 14.sp)
    }
}

fun getIngredientsList(meal: Meal): List<Pair<String, String>> {
    val list = mutableListOf<Pair<String, String>>()
    // Si c'est un repas de la DB, les ingrédients sont dans strIngredient1 (voir mapping)
    if (meal.strIngredient1?.contains(",") == true) {
        meal.strIngredient1.split(",").forEach { list.add(it.trim() to "") }
        return list
    }
    if (!meal.strIngredient1.isNullOrBlank()) list.add(meal.strIngredient1 to (meal.strMeasure1 ?: ""))
    if (!meal.strIngredient2.isNullOrBlank()) list.add(meal.strIngredient2 to (meal.strMeasure2 ?: ""))
    if (!meal.strIngredient3.isNullOrBlank()) list.add(meal.strIngredient3 to (meal.strMeasure3 ?: ""))
    if (!meal.strIngredient4.isNullOrBlank()) list.add(meal.strIngredient4 to (meal.strMeasure4 ?: ""))
    if (!meal.strIngredient5.isNullOrBlank()) list.add(meal.strIngredient5 to (meal.strMeasure5 ?: ""))
    return list
}
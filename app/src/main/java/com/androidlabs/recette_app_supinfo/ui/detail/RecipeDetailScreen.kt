package com.androidlabs.recette_app_supinfo.ui.detail

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: MealViewModel = viewModel()
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val meal by viewModel.selectedMeal.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(recipeId) {
        recipeId?.let { viewModel.getMealById(it) }
    }

    Scaffold(
        topBar = {
            // CETTE BOX EST BLOQUÉE EN HAUT ET NE BOUGE PAS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                // Cherche ton IconButton de retour et mets exactement ça :
                IconButton(
                    onClick = { onNavigateBack() }, // Assure-toi que c'est bien écrit comme ça
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour",
                        tint = PrimaryGreen
                    )
                }
                Text(
                    "Détails",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        meal?.let { currentMeal ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                // --- IMAGE ---
                Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp).clip(RoundedCornerShape(16.dp))) {
                    AsyncImage(
                        model = currentMeal.strMealThumb,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // --- TITRE ---
                Text(
                    text = currentMeal.strMeal,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // --- INGRÉDIENTS ---
                val ingredients = getIngredientsList(currentMeal)
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ingredients (${ingredients.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ingredients.forEach { (name, measure) ->
                        IngredientItem(name = name, description = measure)
                    }
                }

                // --- INSTRUCTIONS ---
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Instructions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = currentMeal.strInstructions ?: "", color = Color.Gray, lineHeight = 20.sp)
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    }
}

@Composable
fun IngredientItem(name: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$name - ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = description, color = Color.Gray, fontSize = 14.sp)
    }
}

fun getIngredientsList(meal: Meal): List<Pair<String, String>> {
    val list = mutableListOf<Pair<String, String>>()
    if (!meal.strIngredient1.isNullOrBlank()) list.add(meal.strIngredient1 to (meal.strMeasure1 ?: ""))
    if (!meal.strIngredient2.isNullOrBlank()) list.add(meal.strIngredient2 to (meal.strMeasure2 ?: ""))
    if (!meal.strIngredient3.isNullOrBlank()) list.add(meal.strIngredient3 to (meal.strMeasure3 ?: ""))
    if (!meal.strIngredient4.isNullOrBlank()) list.add(meal.strIngredient4 to (meal.strMeasure4 ?: ""))
    if (!meal.strIngredient5.isNullOrBlank()) list.add(meal.strIngredient5 to (meal.strMeasure5 ?: ""))
    if (!meal.strIngredient6.isNullOrBlank()) list.add(meal.strIngredient6 to (meal.strMeasure6 ?: ""))
    if (!meal.strIngredient7.isNullOrBlank()) list.add(meal.strIngredient7 to (meal.strMeasure7 ?: ""))
    if (!meal.strIngredient8.isNullOrBlank()) list.add(meal.strIngredient8 to (meal.strMeasure8 ?: ""))
    if (!meal.strIngredient9.isNullOrBlank()) list.add(meal.strIngredient9 to (meal.strMeasure9 ?: ""))
    if (!meal.strIngredient10.isNullOrBlank()) list.add(meal.strIngredient10 to (meal.strMeasure10 ?: ""))
    return list
}
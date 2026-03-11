package com.androidlabs.recette_app_supinfo.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Ta couleur principale Tailwind ("primary": "#13ec13")
val PrimaryGreen = Color(0xFF13EC13)
val BackgroundLight = Color(0xFFF6F8F6)

@Composable
fun RecipeDetailScreen(recipeId: String?, onNavigateBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
    ) {
        // --- 1. TOP BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryGreen.copy(alpha = 0.1f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
            }

            Text(
                text = "Recipe Details",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp), // Pour centrer le texte malgré le bouton à gauche
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // --- 2. HERO SECTION (Image + Titre) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Image de fond (Placeholder pour l'instant)
            AsyncImage(
                model = "https://www.themealdb.com/images/media/meals/1529446352.jpg", // Image d'exemple
                contentDescription = "Chicken",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dégradé sombre pour lire le texte
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            // Textes sur l'image
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge(text = "MAIN COURSE", color = PrimaryGreen)
                    Badge(text = "THAI CUISINE", color = Color.White.copy(alpha = 0.3f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Spicy Basil Chicken (Pad Krapow)",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "Time", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("25 mins", color = Color.White, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. INGREDIENTS ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ingredients", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("8 items", color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Liste statique pour voir le design
            IngredientItem("500g Chicken Breast", "Minced or finely chopped")
            IngredientItem("2 cups Thai Holy Basil", "Fresh leaves only")
            IngredientItem("5 cloves Garlic", "Minced")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. INSTRUCTIONS (Timeline) ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Instructions", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            InstructionItem(
                step = 1,
                title = "Prep the Aromatics",
                description = "Pound the garlic and Thai chilies together using a mortar and pestle until a coarse paste forms. This releases the essential oils for better flavor.",
                isLast = false
            )
            InstructionItem(
                step = 2,
                title = "Stir-Fry Chicken",
                description = "Heat oil in a wok over high heat. Add the chili-garlic paste and stir-fry for 30 seconds until fragrant. Add minced chicken and cook until no longer pink.",
                isLast = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// --- COMPOSANTS REUTILISABLES ---

@Composable
fun Badge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun IngredientItem(name: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PrimaryGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(description, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun InstructionItem(step: Int, title: String, description: String, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Colonne de gauche (Le numéro et la ligne)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(step.toString(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(100.dp) // Hauteur arbitraire pour la ligne
                        .background(PrimaryGreen.copy(alpha = 0.2f))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Colonne de droite (Le texte)
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}
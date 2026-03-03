package com.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.recipeapp.ui.Screen
import com.recipeapp.ui.detail.DetailScreen
import com.recipeapp.ui.detail.DetailViewModel
import com.recipeapp.ui.detail.DetailViewModelFactory
import com.recipeapp.ui.main.MainScreen
import com.recipeapp.ui.main.MainViewModel
import com.recipeapp.ui.main.MainViewModelFactory
import com.recipeapp.ui.splash.SplashScreen
import com.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as RecipeApplication).repository)
    }

    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory((application as RecipeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                var currentScreen: Screen by remember { mutableStateOf(Screen.Splash) }

                when (val screen = currentScreen) {
                    is Screen.Splash -> {
                        SplashScreen(onFinished = { currentScreen = Screen.Main })
                    }
                    is Screen.Main -> {
                        MainScreen(
                            viewModel = mainViewModel,
                            onMealClick = { mealId -> currentScreen = Screen.Detail(mealId) }
                        )
                    }
                    is Screen.Detail -> {
                        DetailScreen(
                            mealId = screen.mealId,
                            viewModel = detailViewModel,
                            onBack = { currentScreen = Screen.Main }
                        )
                    }
                }
            }
        }
    }
}

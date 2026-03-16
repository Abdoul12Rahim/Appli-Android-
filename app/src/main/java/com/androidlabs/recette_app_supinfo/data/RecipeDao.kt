package com.androidlabs.recette_app_supinfo.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    // Cette fonction récupère toutes les recettes stockées
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    // Cette fonction sauvegarde les recettes.
    // OnConflictStrategy.REPLACE permet de mettre à jour si la recette existe déjà.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)
}
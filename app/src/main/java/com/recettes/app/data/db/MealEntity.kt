package com.recettes.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val name: String,
    val thumbnail: String,
    val category: String,
    val area: String,
    val instructions: String,
    val ingredientsJson: String,   // JSON array of "measure ingredient"
    val tags: String,
    val cachedAt: Long = System.currentTimeMillis()
)

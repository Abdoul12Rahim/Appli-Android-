package com.recettes.app.data.model

import com.google.gson.annotations.SerializedName

data class MealListResponse(
    @SerializedName("meals") val meals: List<Meal>?
)

data class Meal(
    @SerializedName("idMeal")    val id: String,
    @SerializedName("strMeal")   val name: String,
    @SerializedName("strMealThumb") val thumbnail: String,
    // Returned by search and lookup, not by filter
    @SerializedName("strCategory") val category: String? = null,
    @SerializedName("strArea")     val area: String? = null
)

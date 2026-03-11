package com.recettes.app.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("categories") val categories: List<Category>
)

data class Category(
    @SerializedName("idCategory")    val id: String,
    @SerializedName("strCategory")  val name: String,
    @SerializedName("strCategoryThumb") val thumbnail: String,
    @SerializedName("strCategoryDescription") val description: String
)

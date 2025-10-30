package org.rodbergrsquared.recipebox.data.model

data class RecipeEntry(
    val recipeId: Int,
    val cardIndex: Int,
    val quantity: String,
    val units: String,
    val ingredient: String,
    val note: String
)

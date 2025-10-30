package org.rodbergrsquared.recipebox.data.model

data class FullRecipe(
    val recipe: Recipe,
    val entries: List<RecipeEntry>
)
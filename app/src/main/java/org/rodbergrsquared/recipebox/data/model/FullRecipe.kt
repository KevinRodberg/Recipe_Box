package org.rodbergrsquared.recipebox.data.model

/** Reresents a complete recipe
 *      by combining Recipe and RecipeEntry. */
data class FullRecipe(
    val recipe: Recipe,
    val entries: List<RecipeEntry>
)


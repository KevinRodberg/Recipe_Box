package org.rodbergrsquared.recipebox.data.datasource

import org.rodbergrsquared.recipebox.data.model.*

/** Static recipe loader */
object StaticRecipeLoader {
    val recipes = listOf(
        Recipe(1, "Demo", "Chocolate Chip Oatmeal Cookies", "Demonstration"),
        Recipe(2, "Demo", "Pie Crust", "Demonstration")
    )

    val entries = listOf(
        RecipeEntry(1, 1, "1", "cup", "flour", ""),
        RecipeEntry(1, 2, "2", "step", "Mix dry ingredients", ""),
        RecipeEntry(2, 1, "3-1/2", "cup", "flour", "-- all purpose") ,
        RecipeEntry(2, 2, "6", "tablespoon", "butter", "very cold"),
        RecipeEntry(2, 3, "3", "tablespoon", "Criso", "very cold"),
        RecipeEntry(2, 4, "1", "step", "Dice butter and crisco", "1/4-1/2 peices"),
        RecipeEntry(2, 5, "2", "step", "Combine flour and butter in mixer", "very slowly"))
}
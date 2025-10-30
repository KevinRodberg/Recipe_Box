package org.rodbergrsquared.recipebox.data

import android.content.Context
import android.net.Uri
import org.rodbergrsquared.recipebox.data.datasource.CsvFileDataSource
import org.rodbergrsquared.recipebox.data.datasource.StaticRecipeLoader
import org.rodbergrsquared.recipebox.data.model.FullRecipe
import org.rodbergrsquared.recipebox.data.model.Recipe
import org.rodbergrsquared.recipebox.data.model.RecipeEntry

// For now, it will use the static loader.
class RecipeRepository(context: Context)  {

    private val csvDataSource = CsvFileDataSource(context)
    private var recipes: List<Recipe> = emptyList()
    private var entries: List<RecipeEntry> = emptyList()

    fun loadDataFromCsv(recipeUri: Uri, entryUri: Uri) {
        recipes = csvDataSource.loadRecipes(recipeUri)
        entries = csvDataSource.loadEntries(entryUri)
    }

    // getFullRecipes now works on the data loaded into memory.
    fun getFullRecipes(): List<FullRecipe> {
        val entriesByRecipeId = entries.groupBy { it.recipeId }

        return recipes.map { recipe ->
            FullRecipe(
                recipe = recipe,
                entries = entriesByRecipeId[recipe.id] ?: emptyList()
            )
        }
    }

    fun oldgetFullRecipes(): List<FullRecipe> {
        val allRecipes = StaticRecipeLoader.recipes
        val allEntries = StaticRecipeLoader.entries

        // The 'groupBy' function creates a Map where the
        // key is the recipeId and the value is a list of entries for that recipe.
        val entriesByRecipeId = allEntries.groupBy { it.recipeId }

        // Now, map each Recipe to a FullRecipe, attaching its corresponding entries.
        return allRecipes.map { recipe ->
            FullRecipe(
                recipe = recipe,
                entries = entriesByRecipeId[recipe.id] ?: emptyList() // Use the entries or an empty list if none exist.
            )
        }
    }

    // You can add more functions here later, like filtering by category.
    fun getRecipesByCategory(category: String): List<FullRecipe> {
        return getFullRecipes().filter { it.recipe.category.equals(category, ignoreCase = true) }
    }

    // A function to get a single recipe by its ID. This will be useful when you click on a recipe.
    fun getRecipeById(id: Int): FullRecipe? {
        return getFullRecipes().find { it.recipe.id == id }
    }
}
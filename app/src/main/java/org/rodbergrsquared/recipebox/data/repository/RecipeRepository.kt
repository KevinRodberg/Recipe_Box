package org.rodbergrsquared.recipebox.data.repository

import android.content.Context
import android.net.Uri
import org.rodbergrsquared.recipebox.data.datasource.CsvFileDataSource
import org.rodbergrsquared.recipebox.data.datasource.StaticRecipeLoader
import org.rodbergrsquared.recipebox.data.model.FullRecipe
import org.rodbergrsquared.recipebox.data.model.Recipe
import org.rodbergrsquared.recipebox.data.model.RecipeEntry

class RecipeRepository(context: Context) {

    private val csvDataSource = CsvFileDataSource(context)
    private var recipes: List<Recipe> = emptyList()
    private var entries: List<RecipeEntry> = emptyList()
    private var useStaticData = true

    init {
        // Load static data by default when the repository is created.
        recipes = StaticRecipeLoader.recipes
        entries = StaticRecipeLoader.entries
    }

    fun loadDataFromCsv(recipeUri: Uri, entryUri: Uri): Boolean {
        return try {
            // Load into temporary lists first. This is safer.
            val tempRecipes = csvDataSource.loadRecipes(recipeUri)
            val tempEntries = csvDataSource.loadEntries(entryUri)

            // Only commit the data and change the state if both files were loaded
            // and contain data. This prevents a failed load from wiping the app.
            if (tempRecipes.isNotEmpty() && tempEntries.isNotEmpty()) {
                this.recipes = tempRecipes
                this.entries = tempEntries
                this.useStaticData = false // The switch happens here, only on success.
                true // Return true for success
            } else {
                false // Return false if files were empty or loading failed.
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false on any exception.
        }
    }

    fun getRecipeHeaders(): List<FullRecipe> {
        val recipeSource = if (useStaticData) StaticRecipeLoader.recipes else recipes
        return recipeSource.map { recipe ->
            FullRecipe(recipe = recipe, entries = emptyList())
        }
    }

    fun getRecipeById(id: Int): FullRecipe? {
        return getFullRecipes().find { it.recipe.id == id }
    }

    fun getFullRecipes(): List<FullRecipe> {
        val recipeSource = if (useStaticData) StaticRecipeLoader.recipes else recipes
        val entrySource = if (useStaticData) StaticRecipeLoader.entries else entries

        val entriesByRecipeId = entrySource.groupBy { it.recipeId }

        return recipeSource.map { recipe ->
            FullRecipe(
                recipe = recipe,
                entries = entriesByRecipeId[recipe.id] ?: emptyList()
            )
        }
    }
}

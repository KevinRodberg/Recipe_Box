package org.rodbergrsquared.recipebox.data.repository

import android.content.Context
import android.net.Uri
import org.rodbergrsquared.recipebox.data.datasource.CsvFileDataSource
import org.rodbergrsquared.recipebox.data.datasource.StaticRecipeLoader
import org.rodbergrsquared.recipebox.data.model.FullRecipe
import org.rodbergrsquared.recipebox.data.model.Recipe
import org.rodbergrsquared.recipebox.data.model.RecipeEntry

/**
 * Repository for recipes and recipe entries.
 */
class RecipeRepository(context: Context) {

    private val csvDataSource = CsvFileDataSource(context)
    private var recipes: List<Recipe> = emptyList()
    private var entries: List<RecipeEntry> = emptyList()
    private var useStaticData = true

    // Load static data by default when the repository is created.
    init {
        recipes = StaticRecipeLoader.recipes
        entries = StaticRecipeLoader.entries
    }

    /** Load recipe lists and detailed entries from CSV files.
     *      @param recipeUri The URI of the CSV file containing recipe data.
     *      @param entryUri The URI of the CSV file containing detailed entry data.
     *      @return True if the data was loaded successfully, false otherwise.
     */
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

    /**  Getters for recipes headers only.
     *      @return A list of FullRecipe objects representing the recipes.
     */
    fun getRecipeHeaders(): List<FullRecipe> {
        val recipeSource = if (useStaticData) StaticRecipeLoader.recipes else recipes
        return recipeSource.map { recipe ->
            FullRecipe(recipe = recipe, entries = emptyList())
        }
    }

    /**  Getters for a single recipe by ID.
     *      @param id The ID of the recipe to retrieve.
     *      @return A list of FullRecipe objects representing a single recipe.
     */
    fun getRecipeById(id: Int): FullRecipe? {
        return getFullRecipes().find { it.recipe.id == id }
    }

    /**  Getters for detailed recipes and entries.
     *      @return A list of RecipeEntry objects representing detailed entries.
     */
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

package org.rodbergrsquared.recipebox.ui

/**
 * An interface to handle clicks on items in the recipe list.
 */
interface OnRecipeClickListener {
    /**
     * Called when a recipe in the list is selected.
     * @param recipeId The unique ID of the selected recipe.
     */
    fun onRecipeSelected(recipeId: Int)
}

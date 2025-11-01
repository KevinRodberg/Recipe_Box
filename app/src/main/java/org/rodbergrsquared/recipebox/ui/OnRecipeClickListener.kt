package org.rodbergrsquared.recipebox.ui

/** An interface to handle clicks on items in the recipe list.
 *      @param recipeId The unique ID of the selected recipe. */
interface OnRecipeClickListener {
     fun onRecipeSelected(recipeId: Int)
}

package org.rodbergrsquared.recipebox.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.rodbergrsquared.recipebox.R
import org.rodbergrsquared.recipebox.data.model.FullRecipe

class RecipeAdapter(
    private var fullRecipes: List<FullRecipe>,
    private val clickListener: OnRecipeClickListener
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.recipeName)
        val categoryText: TextView = itemView.findViewById(R.id.recipeCategory)
        val creditText: TextView = itemView.findViewById(R.id.recipeCredit)
        val ingredientsRecyclerView: RecyclerView = itemView.findViewById(R.id.ingredientsRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    fun updateData(newRecipes: List<FullRecipe>) {
        fullRecipes = newRecipes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val fullRecipe = fullRecipes[position]

        holder.nameText.text = fullRecipe.recipe.name
        holder.categoryText.text = fullRecipe.recipe.category
        holder.creditText.text = fullRecipe.recipe.credit

        // Set up the nested RecyclerView for ingredients and steps.
        val ingredientAdapter = IngredientAdapter(fullRecipe.entries)
        holder.ingredientsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.ingredientsRecyclerView.adapter = ingredientAdapter

        // When an item in the list is clicked, notify the listener.
        holder.itemView.setOnClickListener {
            clickListener.onRecipeSelected(fullRecipe.recipe.id)
        }
    }

    override fun getItemCount() = fullRecipes.size
}

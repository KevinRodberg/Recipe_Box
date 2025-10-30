package org.rodbergrsquared.recipebox.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.rodbergrsquared.recipebox.R
import org.rodbergrsquared.recipebox.data.model.*
import org.rodbergrsquared.recipebox.data.*
import kotlin.io.path.name

class RecipeAdapter(private var fullRecipes: List<FullRecipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
        class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameText:
                    TextView = itemView.findViewById(R.id.recipeName)
            val categoryText:
                    TextView = itemView.findViewById(R.id.recipeCategory)
            val creditText:
                    TextView = itemView.findViewById(R.id.recipeCredit)
            val entriesPreviewTextView:
                    TextView = itemView.findViewById(R.id.entriesPreviewTextView)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipe, parent, false)
            return RecipeViewHolder(view)
        }

        fun updateData(newRecipes: List<FullRecipe>) {
            fullRecipes = newRecipes
            notifyDataSetChanged() // This tells the RecyclerView to completely redraw itself.
        }

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            val fullRecipe = fullRecipes[position]

            val entriesPreviewText = fullRecipe.entries.joinToString(separator = "\n") { entry ->
                // Check if the unit is 'step'
                if (entry.units.equals("step", ignoreCase = true)) {
                    // It's a step: display the ingredient and note.
                    // We can check if the note is blank to avoid printing empty parentheses.
                    if (entry.note.isNotBlank()) {
                        "${entry.ingredient} (${entry.note})"
                    } else {
                        entry.ingredient
                    }
                } else {
                    // It's an ingredient: display the original format.
                    "• ${entry.quantity} ${entry.units} ${entry.ingredient}"
                }
            }

            holder.nameText.text = fullRecipe.recipe.name
            holder.categoryText.text = fullRecipe.recipe.category
            holder.creditText.text = fullRecipe.recipe.credit
            holder.entriesPreviewTextView.text = entriesPreviewText
        }

    override fun getItemCount() = fullRecipes.size
    }
